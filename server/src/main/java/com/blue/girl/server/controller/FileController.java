package com.blue.girl.server.controller;

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

@RestController
@RequestMapping("/api/file")
@Log4j2
public class FileController {

    @Value("${download.url}")
    protected String downloadUrl;

    @Autowired
    FileRecordServiceImpl fileRecordService;

    /**
     * 下载服务器本地的文件
     */
    @GetMapping(value = "/download/{fileId}")
    public void downloadLocalServerFile(@PathVariable Integer fileId, HttpServletResponse response) throws IOException {
        if(null != fileId){
            fileRecordService.downloadLocalServerFile(fileId, response);
        } else {
            throw new BusinessException("003", "下载失败，请重试！");
        }
    }

//    /**
//     * 上传文件到服务器，保存在服务器本地
//     */
//    @PostMapping("/upload")
//    public ApiResult uploadFileToLocalServer(@RequestParam("file") MultipartFile file) {
//        return ApiResult.T(fileRecordService.uploadFileToLocalServer(file, downloadUrl));
//    }

}
