package com.blue.girl.server.service;

import com.blue.girl.server.entity.FileRecordEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileRecordService {

    void downloadLocalServerFile(int fileId, HttpServletResponse response) throws IOException;

    String  uploadFileToLocalServer(MultipartFile multipartFile, String downloadUrl);

}
