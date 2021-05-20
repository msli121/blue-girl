package com.blue.girl.server.service;

import com.blue.girl.server.dto.TagMergeRequest;
import com.blue.girl.server.entity.FileRecordEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileRecordService {

    void downloadLocalServerFile(int fileId, HttpServletResponse response) throws IOException;

    String  uploadFileToLocalServer(MultipartFile multipartFile, String downloadUrl);

    FileRecordEntity getMergedPhotoUrl(MultipartFile multipartFile, String bgKey, String apiUrl, String downloadUrl);

    FileRecordEntity getLocalMergePhotoUrl(MultipartFile multipartFile, String bgKey, String apiUrl, String downloadUrl);

    FileRecordEntity getQrCodeUrl(String photoUrl, String downloadUrl);

    FileRecordEntity getMergedPhotoWithTags(TagMergeRequest request, String downloadUrl);

}
