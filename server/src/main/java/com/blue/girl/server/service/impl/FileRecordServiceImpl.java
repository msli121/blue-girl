package com.blue.girl.server.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.girl.server.dto.TagMergeRequest;
import com.blue.girl.server.entity.FileRecordEntity;
import com.blue.girl.server.exception.BusinessException;
import com.blue.girl.server.service.BaseService;
import com.blue.girl.server.service.FileRecordService;
import com.blue.girl.server.utils.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service("FileRecordServiceImpl")
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class FileRecordServiceImpl extends BaseService implements FileRecordService {

    @Autowired
    private BaseCache baseCache;

    private static HashMap<String, String> imageContentType= new HashMap<>();

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
    public void downloadLocalServerFile(int fileId, HttpServletResponse response) {
        FileRecordEntity fileRecord = fileRecordDao.findById(fileId);
        if(null != fileRecord) {
            response.setDateHeader("Expires", 0L);

            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

            response.addHeader("Cache-Control", "post-check=0, pre-check=0");

            response.setHeader("Pragma", "no-cache");

            response.setContentType("image/png");

            try {
                File originFile = new File(fileRecord.getLocalAddress());
                BufferedImage originBufferImage = ImageIO.read(originFile);
                // ????????????
                int originWidth = originBufferImage.getWidth();
                int originHeight = originBufferImage.getHeight();
                // ????????????????????????
                int newWidth = (int) (originWidth * 0.5);
                int newHeight = (int) (originHeight * 0.5);
                Image scaledImage = originBufferImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                // ???????????????
                BufferedImage newBufferImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D canvas = newBufferImage.createGraphics();
                // ????????????
                canvas.drawImage(scaledImage, 0, 0, null);
                // ??????????????????
                canvas.dispose();
                // ????????????????????????
                String fileName = originFile.getName();
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                fileType = StringUtils.isEmpty(fileType) ? "png" : fileType;
                response.setContentType(imageContentType.get(fileType));
                log.info("???????????? >> ???????????????????????? >> ????????????[{}] ???????????????[{}]", fileName, fileType);
                ImageIO.write(newBufferImage, fileType, response.getOutputStream());
//                File image = new File(fileRecord.getLocalAddress());
//                FileInputStream inputStream = new FileInputStream(image);
//                int length = inputStream.available();
//                byte data[] = new byte[length];
//                response.setContentLength(length);
//                String fileName = image.getName();
//                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//                response.setContentType(imageContentType.get(fileType));
//                inputStream.read(data);
//                OutputStream toClient = response.getOutputStream();
//                toClient.write(data);
//                toClient.flush();
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException("-1", "???????????????");
            }
        } else {
            throw new BusinessException("-1", "???????????????");
        }
    }

    /**
     * ??????????????????????????????
     * @param multipartFile
     * @param downloadUrl
     * @return
     */
    @Override
    public String uploadFileToLocalServer(MultipartFile multipartFile, String downloadUrl) {
        String fileRootDir = generateFileRootDir();
        // ???????????????????????????
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
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            log.info("localAddress >>>>>>>>>>>>>>>>>>>> " + fileRecord.getLocalAddress());
            // ????????????????????????
            fileRecordDao.save(fileRecord);
            return downloadUrl + "/" + fileRecord.getId();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("001", "???????????????????????????");
        }
    }

    /**
     * ????????????????????? url
     * @param photoUrl
     * @param downloadUrl
     * @return
     */
    @Override
    public FileRecordEntity getQrCodeUrl(String photoUrl, String downloadUrl) {
        try {
            // ???????????? logo ??????
            ClassPathResource classPathResource = new ClassPathResource("static/logo.png");
            // ?????? logo ???????????????
            InputStream logoInputStream = classPathResource.getInputStream();
            // ???????????????????????????????????? url
            String fileName = SysRandomUtil.getRandomString(16) + ".png";
            String fileRootDir = generateFileRootDir();
            File newFile = new File(fileRootDir, fileName);
            QRCodeUtil.encode(photoUrl, logoInputStream, newFile);
            // ????????????????????????
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setFileName(fileName);
            fileRecord.setLocalAddress(fileRootDir + File.separator + fileName);
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            // ??????????????????
            fileRecordDao.save(fileRecord);
            fileRecord.setFileUrl(downloadUrl + "/" + fileRecord.getId());
            fileRecordDao.save(fileRecord);
            log.info("========== ?????????????????????????????? " + fileRecord.getLocalAddress());
            // ????????????????????? url
            return fileRecordDao.save(fileRecord);
        } catch (Exception e) {
            e.getStackTrace();
            throw new BusinessException("-1", "?????????????????????????????????");
        }
    }

    /**
     * ????????????????????? id ??? tag ??????
     * @param request
     * @param downloadUrl
     * @return
     */
    @Override
    public FileRecordEntity getMergedPhotoWithTags(TagMergeRequest request, String downloadUrl) {
        FileRecordEntity mainPhoto = fileRecordDao.findById(request.getFileId());
        if(null == mainPhoto || StringUtils.isEmpty(mainPhoto.getLocalAddress())) {
            throw new BusinessException("-1", "???????????????");
        }
        // ????????????
        File mainFile = new File(mainPhoto.getLocalAddress());
        try {
            // ????????????
            BufferedImage mainImage = ImageIO.read(mainFile);
            log.info("?????????????????? width = [{}], height = [{}] >> ", mainImage.getWidth(), mainImage.getHeight());
            // ????????????
            Graphics2D canvas = mainImage.createGraphics();
            request.getTags().forEach(tagItem -> {
                try {
                    log.info("???????????? >> ???" + tagItem.getTagKey());
                    BufferedImage tagImage = (BufferedImage) baseCache.getTenHoursCache().get(tagItem.getTagKey(), () -> {
                        String tagFilePath = "static/" + tagItem.getTagKey();
                        // ???????????? tag ??????
                        ClassPathResource classPathResource = new ClassPathResource(tagFilePath);
                        BufferedImage bufferedImage = ImageIO.read(classPathResource.getInputStream());
                        return bufferedImage;
                    });
                    // ????????????
                    log.info("????????????  width = [{}], height = [{}] >> " , tagImage.getWidth(), tagImage.getHeight());
                    // ?????? tag ???????????????
                    canvas.drawImage(tagImage, tagItem.getLeftStart(), tagItem.getTopStart(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BusinessException("-1", "??????????????????");
                }
            });
            // ??????????????????????????????????????????
            canvas.dispose();
            // ?????????????????????
            return saveBufferImageToLocalServer(mainImage, downloadUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("-1", "??????????????????");
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
     * @param multipartFile
     * @param bgKey
     * @param apiUrl
     * @return ??????????????????????????????
     */
    @Override
    public FileRecordEntity getMergedPhotoUrl(MultipartFile multipartFile, String bgKey, String apiUrl, String downloadUrl){
        if(StringUtils.isEmpty(bgKey) || multipartFile.isEmpty()) {
            throw new BusinessException("-1", "???????????????????????????????????????");
        }
        try {
            // ????????????
            BufferedImage mainImage = ImageIO.read(multipartFile.getInputStream());
            // ????????????????????????
            log.info("????????????????????????with = " + mainImage.getWidth() + " height = " + mainImage.getHeight());
            String backgroundBase64 = (String) baseCache.getTenHoursCache().get(bgKey, () -> {
                String localPhotoUrl = "static/" + bgKey;
                // ???????????? logo ??????
                ClassPathResource classPathResource = new ClassPathResource(localPhotoUrl);
                // Base64 ??????
                InputStream inputStream =classPathResource.getInputStream();
                String base64Str = Base64Util.inputStreamToBase64Str(inputStream);
                // ?????? \r\n
                base64Str = base64Str.replaceAll("[\\s*\t\n\r]", "");
                // ?????? ??????
                base64Str =  base64Str.contains(",") ? base64Str.split(",")[1] : base64Str;
                return base64Str;
            });
            // Base64 ??????
            String photoBase64 = Base64Util.inputStreamToBase64Str(multipartFile.getInputStream());
            // ?????? \r\n
            photoBase64 = photoBase64.replaceAll("[\\s*\t\n\r]", "");
            // ????????????
            photoBase64 = photoBase64.contains(",") ? photoBase64.split(",")[1] : photoBase64;
            // ????????????????????????
            log.info("========== ??????????????????????????????");
            long startTime = System.currentTimeMillis();
            String mergeResult = getMergeResult(photoBase64, backgroundBase64, apiUrl);
            log.info("========== ???????????????????????? ?????????" + (System.currentTimeMillis() - startTime) + "ms");
            // ???????????????????????? url
            if(null == mergeResult || mergeResult.contains("error")) {
                throw new BusinessException("-1", "AI??????????????????");
            }
            return saveBase64FileToLocalServer(mergeResult, downloadUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("-1", e.getMessage());
        }
    }

    /**
     * ??????????????????
     * @param multipartFile
     * @param bgKey
     * @param apiUrl
     * @param downloadUrl
     * @return
     */
    @Override
    public FileRecordEntity getLocalMergePhotoUrl(MultipartFile multipartFile, String bgKey, String apiUrl, String downloadUrl) {
        if(StringUtils.isEmpty(bgKey) || multipartFile.isEmpty()) {
            throw new BusinessException("-1", "???????????????????????????????????????");
        }
        String bgName = bgKey.replace("jpg", "png");
        try {
            // ????????????
            BufferedImage uploadBufferedImage = ImageIO.read(multipartFile.getInputStream());
            // ????????????????????????
            log.info("????????????????????????with = " + uploadBufferedImage.getWidth() + " height = " + uploadBufferedImage.getHeight());
            // ????????????
            Graphics2D canvas = uploadBufferedImage.createGraphics();
            // ??????????????????
            try {
                log.info("?????????????????? >> ???" + bgName);
                BufferedImage bgBufferImage = (BufferedImage) baseCache.getTenHoursCache().get(bgName, () -> {
                    String tagFilePath = "static/" + bgName;
                    log.info("???????????? >> ???" + tagFilePath);
                    // ????????????????????????
                    ClassPathResource classPathResource = new ClassPathResource(tagFilePath);
                    BufferedImage bufferedImage = ImageIO.read(classPathResource.getInputStream());
                    return bufferedImage;
                });
                // ??????????????????
                log.info("??????????????????  width = [{}], height = [{}] >> " , bgBufferImage.getWidth(), bgBufferImage.getHeight());
                // ??????????????????????????????
                canvas.drawImage(bgBufferImage, 0, 0, null);
                // ??????????????????????????????????????????
                canvas.dispose();
                // ?????????????????????
                return saveBufferImageToLocalServer(uploadBufferedImage, downloadUrl);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("-1", "????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("-1", e.getMessage());
        }
    }


    private String generateFileRootDir() {
        String os = System.getProperty("os.name");
        String fileRootDir;
        if(os.toLowerCase().startsWith("win")) {
            fileRootDir = "C:"+ File.separator + "blue-girl" + File.separator + "upload-picture";
        } else if(os.toLowerCase().contains("mac")) {
            fileRootDir = "/Users/liulin/Documents/blue-girl";
        } else {
            fileRootDir = "/opt/blue-girl/upload-picture";
        }
        return fileRootDir;
    }

    private String getMergeResult(String photoBase64, String backgroundBase64, String apiUrl) {
        // ???????????????
        ArrayList<String> requestData = new ArrayList<>();
        requestData.add(photoBase64);
        requestData.add(backgroundBase64);
        // ????????????
        String mergeResult;
        try {
            mergeResult = SysHttpUtils.getInstance().sendJsonPost(apiUrl, JSON.toJSONString(requestData));
            return mergeResult;
        } catch (Exception e) {
            throw new BusinessException("-1", "?????????????????????????????????????????????");
        }
    }

    private FileRecordEntity saveBufferImageToLocalServer(BufferedImage image, String downloadUrl) {
        // ??????????????????
        try {
            String fileRootDir = generateFileRootDir() + File.separator;
            String fileName = SysRandomUtil.getRandomString(16) + ".png";
            File newFile = new File(fileRootDir + fileName);
            ImageIO.write(image, "png", newFile);
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setFileName(fileName);
            fileRecord.setLocalAddress(fileRootDir + fileName);
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            // ????????????????????????
            fileRecordDao.save(fileRecord);
            fileRecord.setFileUrl(downloadUrl + "/" + fileRecord.getId());
            fileRecordDao.save(fileRecord);
            return fileRecord;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("-1", "???????????????????????????");
        }
    }

    private FileRecordEntity saveBase64FileToLocalServer(String resultStr, String downloadUrl) {
        // ????????????
        if(StringUtils.isEmpty(resultStr) || !resultStr.contains("base64")) {
            throw new BusinessException("-1", "????????????????????????");
        }
        // jsonStr -> JsonObject
        JSONObject mergedObj = JSON.parseObject(resultStr);
        String base64Str = mergedObj.getString("base64");
        // ??????????????????
        String fileRootDir = generateFileRootDir() + File.separator;
        String fileName = SysRandomUtil.getRandomString(16) + ".jpg";
        // ???????????????
        try {
            Base64Util.base64ContentToFile(base64Str, fileRootDir + fileName);
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setFileName(fileName);
            fileRecord.setLocalAddress(fileRootDir + fileName);
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            // ????????????????????????
            fileRecordDao.save(fileRecord);
            fileRecord.setFileUrl(downloadUrl + "/" + fileRecord.getId());
            fileRecordDao.save(fileRecord);
            return fileRecord;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("-1", "???????????????????????????");
        }
    }
}
