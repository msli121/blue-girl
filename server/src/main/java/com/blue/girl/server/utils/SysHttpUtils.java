package com.blue.girl.server.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * @Description
 * @Author msli
 * @Date 2021/04/13
 */

public class SysHttpUtils {
    private final static Log log = LogFactory.getLog(SysHttpUtils.class);

    private static SysHttpUtils instance = null;

    public static SysHttpUtils getInstance() {
        if (instance == null) {
            instance = new SysHttpUtils();
        }
        return instance;
    }

    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(600000)
            .setConnectTimeout(600000)
            .setConnectionRequestTimeout(600000)
            .build();

    public static void setTimeOutTime(int time) {
        if (instance != null) {
            instance.requestConfig = RequestConfig.custom()
                    .setSocketTimeout(time)
                    .setConnectTimeout(time)
                    .setConnectionRequestTimeout(time)
                    .build();
        }
    }

    /**
     * ?????? get?????? ?????????URL
     * @param httpUrl
     * @return
     */
    public String sendHttpGet(String httpUrl) {
        // ??????get??????
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpBase(httpGet);
    }

    /**
     * ?????? get?????? URL?????????map??????????????????url??????
     * @param httpUrl  ??????
     * @param params  ??????
     * @return
     */
    public String sendHttpGet(String httpUrl, Map<String, String> params) {
        // ??????get??????
        HttpGet httpGet = new HttpGet(urlParamSplice(httpUrl, params));
        return sendHttpBase(httpGet);
    }


    /**
     * ?????? get?????? URL?????????map????????????????????????????????????url??????
     * @param httpUrl ??????
     * @param params ??????
     * @param headers ???????????????
     * @return
     */
    public String sendHttpGet(String httpUrl, Map<String, String> params, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(urlParamSplice(httpUrl, params));// ??????get??????
        return sendHttpBase(httpGet, headers);
    }

    /**
     * ???????????? post ??????
     * @param httpUrl ??????
     * @return
     */
    public String sendHttpPost(String httpUrl) {
        // ??????httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpBase(httpPost);
    }

    /**
     * ?????? post json ??????, data ??? jsonString
     * @param httpUrl
     * @param jsonString
     * @return
     */
    public String sendJsonPost(String httpUrl, String jsonString) {
        return sendJsonPost(httpUrl, jsonString, null);
    }

    /**
     * ?????? post json ??????
     * @param httpUrl
     * @param jsonString
     * @param headers
     * @return
     */
    public String sendJsonPost(String httpUrl, String jsonString, Map headers) {
        HttpPost httpPost = new HttpPost(httpUrl);// ??????httpPost
        return sendJsonPost(jsonString, headers, httpPost);
    }

    private String sendJsonPost(String jsonString, Map headers, HttpPost httpPost) {
        try {
            //????????????
            StringEntity jsonEntity = new StringEntity(jsonString, "UTF-8");
            jsonEntity.setContentType("application/json");
            httpPost.setEntity(jsonEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return sendHttpBase(httpPost, headers);
    }


    public String sendHttpBase(HttpRequestBase httpBase) {
        log.debug("????????????httpUrl???" + httpBase.getURI().toString());
        HttpEntity entity = null;
        String responseContent = null;

        // ???????????????httpClient??????
        //CloseableHttpClient httpClient = HttpClients.createDefault()
        try {

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSocketFactory())
                    .build();

            //DNS ?????????
            DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

            //???????????????????????????
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry,dnsResolver);

            connManager.setDefaultMaxPerRoute(6);
            connManager.setMaxTotal(20);
            connManager.closeIdleConnections(120, TimeUnit.SECONDS);
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

            CloseableHttpClient httpClient = httpClientBuilder.setConnectionManager(connManager).disableAutomaticRetries()
                    .build();
            httpBase.setConfig(requestConfig);
            // ????????????
            try (CloseableHttpResponse response = httpClient.execute(httpBase)) {
                entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.debug("????????????" + responseContent);
        return responseContent;
    }

    public String sendHttpBase(HttpRequestBase httpBase, Map<String, String> headers) {
        log.debug("????????????" + JSON.toJSONString(headers));
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (null != headers) {
            for (String key : headers.keySet()) {
                httpBase.setHeader(key, headers.get(key));
            }
        }
        return sendHttpBase(httpBase);
    }

    public String urlParamSplice(String httpUrl, Map<String, String> params) {
        log.debug("???????????????" + JSON.toJSONString(params));
        Boolean firstParam = true;
        try {
            if (null != params) {
                for (String key : params.keySet()) {
                    if (firstParam) {
                        httpUrl += "?" + key + "=" + URLEncoder.encode(params.get(key), "UTF-8");
                        firstParam = false;
                    } else {
                        httpUrl += "&" + key + "=" + URLEncoder.encode(params.get(key), "UTF-8");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return httpUrl;
    }
}
