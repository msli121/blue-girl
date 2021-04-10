package com.blue.girl.server.controller;

import com.blue.girl.server.utils.QRCodeUtil;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Description
 * @Author msli
 * @Date 2021/04/09
 */

@RestController
@RequestMapping("/api/qr-code")
public class QrCodeController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    /**
     * 根据数据生成二维码
     */
    @PostMapping("/code")
    public void createQrCode(@RequestBody Map map) throws IOException {
        ServletOutputStream stream = null;
        String code = MapUtils.getString(map, "code", "empty");
//        String code = request.getParameter("code");
        try {
            stream = response.getOutputStream();
            //使用工具类生成二维码
            QRCodeUtil.encode(code, stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }

    /**
     * 根据数据生成带有logo二维码
     */
    @PostMapping(value = "/code-logo")
    public void createQrCodeWithLogo(@RequestBody Map map) throws Exception {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
//            String logoPath = "D://logo.png";
            //使用工具类生成二维码
//            String code = request.getParameter("code");
            String code = MapUtils.getString(map, "code", "empty");
            // 获取静态 logo 图片
            ClassPathResource classPathResource = new ClassPathResource("static/logo.png");
            InputStream inputStream =classPathResource.getInputStream();
            QRCodeUtil.encode(code, inputStream, outputStream, true);
        } catch (Exception e) {
            e.getStackTrace();

        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

}
