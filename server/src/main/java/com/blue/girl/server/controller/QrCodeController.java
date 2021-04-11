package com.blue.girl.server.controller;

import com.blue.girl.server.exception.BusinessException;
import com.blue.girl.server.service.impl.FileRecordServiceImpl;
import com.blue.girl.server.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @Description
 * @Author msli
 * @Date 2021/04/09
 */
@RestController
@RequestMapping("/api/qr-code")
public class QrCodeController {

    @Value("${download.url}")
    protected String downloadUrl;

    @Autowired
    FileRecordServiceImpl fileRecordService;

    /**
     * 根据数据生成带有logo二维码
     */
    @PostMapping(value = "/code-logo")
    public void createQrCodeWithLogo(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = null;
        try {
            response.setContentType("image/png");
            outputStream = response.getOutputStream();
            // 生成图片下载链接
            String code = fileRecordService.uploadFileToLocalServer(file, downloadUrl);
            // 获取静态 logo 图片
            ClassPathResource classPathResource = new ClassPathResource("static/logo.png");
            // 使用工具类生成二维码
            InputStream inputStream =classPathResource.getInputStream();
            QRCodeUtil.encode(code, inputStream, outputStream, true);
        } catch (Exception e) {
            e.getStackTrace();
            throw new BusinessException("-1", "上传失败，请重试！");
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }


    //    /**
//     * 根据数据生成二维码
//     */
//    @PostMapping("/code")
//    public void createQrCode(@RequestBody Map map, HttpServletResponse response) throws IOException {
//        ServletOutputStream stream = null;
//        String code = MapUtils.getString(map, "code", "empty");
////        String code = request.getParameter("code");
//        try {
//            stream = response.getOutputStream();
//            //使用工具类生成二维码
//            QRCodeUtil.encode(code, stream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                stream.flush();
//                stream.close();
//            }
//        }
//    }
}
