package com.blue.girl.server.service.impl;


import com.blue.girl.server.entity.FileRecordEntity;
import com.blue.girl.server.exception.BusinessException;
import com.blue.girl.server.service.BaseService;
import com.blue.girl.server.service.FileRecordService;
import com.blue.girl.server.utils.SysRandomUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

@Service("FileRecordServiceImpl")
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class FileRecordServiceImpl extends BaseService implements FileRecordService {

    private static HashMap<String, String> imageContentType=new HashMap<>();

    static {
        imageContentType.put("jpg","image/jpeg");

        imageContentType.put("jpeg","image/jpeg");

        imageContentType.put("png","image/png");

        imageContentType.put("tif","image/tiff");

        imageContentType.put("tiff","image/tiff");

        imageContentType.put("ico","image/x-icon");

        imageContentType.put("bmp","image/bmp");

        imageContentType.put("gif","image/gif");
    }

    @Override
    public void downloadLocalServerFile(int fileId, HttpServletResponse response) throws IOException {
        FileRecordEntity fileRecord = fileRecordDao.findById(fileId);
        if(null != fileRecord) {
            response.setDateHeader("Expires", 0L);

            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

            response.addHeader("Cache-Control", "post-check=0, pre-check=0");

            response.setHeader("Pragma", "no-cache");

            response.setContentType("image/jpeg");

            try {
                File image = new File(fileRecord.getLocalAddress());
                FileInputStream inputStream = new FileInputStream(image);
                int length = inputStream.available();
                byte data[] = new byte[length];
                response.setContentLength(length);
                String fileName = image.getName();
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                response.setContentType(imageContentType.get(fileType));
                inputStream.read(data);
                OutputStream toClient = response.getOutputStream();
                toClient.write(data);
                toClient.flush();
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException("", "图片不存在");
            }
        } else {
            throw new BusinessException("", "图片不存在");
        }
    }

    /**
     * 上传文件到本地服务器
     * @param multipartFile
     * @param downloadUrl
     * @return
     */
    @Override
    public String uploadFileToLocalServer(MultipartFile multipartFile, String downloadUrl) {
        String os = System.getProperty("os.name");
        String fileRootDir;
        if(os.toLowerCase().startsWith("win")) {
            fileRootDir = "C:"+ File.separator + "blue-girl" + File.separator + "upload-picture";
        } else if(os.toLowerCase().contains("mac")) {
            fileRootDir = "/Users/liulin/Documents/blue-girl";
        } else {
            fileRootDir = "/opt/blue-girl/upload-picture";
        }
        // 校验文件夹是否存在
        File folder = new File(fileRootDir);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        try {
            String fileOldName = multipartFile.getOriginalFilename();
            String fileNewName;
            if(StringUtils.isEmpty(fileOldName) || fileOldName.lastIndexOf(".") == 0) {
                fileNewName = SysRandomUtil.getRandomString(16);
            } else {
                fileNewName = SysRandomUtil.getRandomString(12) + fileOldName.substring(fileOldName.lastIndexOf("."));
            }
            File newFile = new File(fileRootDir, fileNewName);
            multipartFile.transferTo(newFile);
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setFileName(fileNewName);
            fileRecord.setLocalAddress(fileRootDir + File.separator + fileNewName);
            fileRecord.setUploadTime(new Timestamp(new Date().getTime()));
            log.info("localAddress >>>>>>>>>>>>>>>>>>>> " + fileRecord.getLocalAddress());
            // 保存所有文件记录
            fileRecordDao.save(fileRecord);
            return downloadUrl + "/" + fileRecord.getId();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("001", "上传失败，再试一次");
        }
    }
}
