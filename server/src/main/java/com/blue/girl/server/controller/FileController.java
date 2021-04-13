package com.blue.girl.server.controller;

import com.alibaba.fastjson.JSON;
import com.blue.girl.server.dto.ApiResult;
import com.blue.girl.server.exception.BusinessException;
import com.blue.girl.server.service.impl.FileRecordServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/file")
@Log4j2
public class FileController {

    @Value("${download.url}")
    protected String downloadUrl;

    @Value("${api.url}")
    protected String apiUrl;

    @Autowired
    FileRecordServiceImpl fileRecordService;

    /**
     * 下载服务器本地的文件
     * @param fileId
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/download/{fileId}")
    public void downloadLocalServerFile(@PathVariable Integer fileId, HttpServletResponse response) throws IOException {
        if(null != fileId){
            fileRecordService.downloadLocalServerFile(fileId, response);
        } else {
            throw new BusinessException("003", "下载失败，请重试！");
        }
    }


    /**
     * 上传拍照图片，返回融图的 url
     * @param file
     * @param backgroundPhoto
     * @return 融合后图片 url 和二维码 url
     */
    @PostMapping("/photo")
    public ApiResult uploadPhotoToGetMergePhoto(@RequestParam("file") MultipartFile file, @RequestParam("bk") String backgroundPhoto) {
        log.info(">>>>>>>>>>>>>> 进入图片融合接口");
        long startTime = System.currentTimeMillis();
        String photoUrl = fileRecordService.getMergedPhotoUrl(file, backgroundPhoto, apiUrl, downloadUrl);
        String qrCodeUrl = fileRecordService.getQrCodeUrl(photoUrl, downloadUrl);
        // 保存结果进行返回
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("photoUrl", photoUrl);
        resultMap.put("qrCodeUrl", qrCodeUrl);
        log.info("<<<<<<<<<<<<<<< 退出图片融合接口 用时：" + (System.currentTimeMillis()-startTime) + "ms");
        return ApiResult.T(resultMap);
    }

//    /**
//     * 上传文件到服务器，保存在服务器本地
//     */
//    @PostMapping("/upload")
//    public ApiResult uploadFileToLocalServer(@RequestParam("file") MultipartFile file) {
//        return ApiResult.T(fileRecordService.uploadFileToLocalServer(file, downloadUrl));
//    }

}
