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
    public void downloadLocalServerFile(int fileId, HttpServletResponse response) {
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
                throw new BusinessException("-1", "图片不存在");
            }
        } else {
            throw new BusinessException("-1", "图片不存在");
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
        String fileRootDir = generateFileRootDir();
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
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            log.info("localAddress >>>>>>>>>>>>>>>>>>>> " + fileRecord.getLocalAddress());
            // 保存所有文件记录
            fileRecordDao.save(fileRecord);
            return downloadUrl + "/" + fileRecord.getId();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("001", "上传失败，再试一次");
        }
    }

    /**
     * 获取二维码图片 url
     * @param photoUrl
     * @param downloadUrl
     * @return
     */
    @Override
    public FileRecordEntity getQrCodeUrl(String photoUrl, String downloadUrl) {
        try {
            // 获取静态 logo 图片
            ClassPathResource classPathResource = new ClassPathResource("static/logo.png");
            // 静态 logo 图片输入流
            InputStream logoInputStream = classPathResource.getInputStream();
            // 生成二维码图片文件对应的 url
            String fileName = SysRandomUtil.getRandomString(16) + ".png";
            String fileRootDir = generateFileRootDir();
            File newFile = new File(fileRootDir, fileName);
            QRCodeUtil.encode(photoUrl, logoInputStream, newFile);
            // 记录本次文件操作
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setFileName(fileName);
            fileRecord.setLocalAddress(fileRootDir + File.separator + fileName);
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            // 保存文件记录
            fileRecordDao.save(fileRecord);
            fileRecord.setFileUrl(downloadUrl + "/" + fileRecord.getId());
            fileRecordDao.save(fileRecord);
            log.info("========== 二维码图片的本地地址 " + fileRecord.getLocalAddress());
            // 返回二维码图片 url
            return fileRecordDao.save(fileRecord);
        } catch (Exception e) {
            e.getStackTrace();
            throw new BusinessException("-1", "生成二维码图片链接失败");
        }
    }

    /**
     * 根据给定的图片 id 与 tag 融合
     * @param request
     * @param downloadUrl
     * @return
     */
    @Override
    public FileRecordEntity getMergedPhotoWithTags(TagMergeRequest request, String downloadUrl) {
        FileRecordEntity mainPhoto = fileRecordDao.findById(request.getFileId());
        if(null == mainPhoto || StringUtils.isEmpty(mainPhoto.getLocalAddress())) {
            throw new BusinessException("-1", "图片不存在");
        }
        // 主要文件
        File mainFile = new File(mainPhoto.getLocalAddress());
        try {
            // 主要图片
            BufferedImage mainImage = ImageIO.read(mainFile);
            log.info("上传图片尺寸 width = [{}], height = [{}] >> ", mainImage.getWidth(), mainImage.getHeight());
            // 生成画布
            Graphics2D canvas = mainImage.createGraphics();
            request.getTags().forEach(tagItem -> {
                try {
                    log.info("贴纸名称 >> ：" + tagItem.getTagKey());
                    BufferedImage tagImage = (BufferedImage) baseCache.getTenHoursCache().get(tagItem.getTagKey(), () -> {
                        String tagFilePath = "static/" + tagItem.getTagKey();
                        // 获取静态 tag 图片
                        ClassPathResource classPathResource = new ClassPathResource(tagFilePath);
                        BufferedImage bufferedImage = ImageIO.read(classPathResource.getInputStream());
                        return bufferedImage;
                    });
                    // 贴纸尺寸
                    log.info("贴纸尺寸  width = [{}], height = [{}] >> " , tagImage.getWidth(), tagImage.getHeight());
                    // 绘制 tag 图到基本图
                    canvas.drawImage(tagImage, tagItem.getLeftStart(), tagItem.getTopStart(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BusinessException("-1", "获取贴纸失败");
                }
            });
            // 释放图形上下文使用的系统资源
            canvas.dispose();
            // 输出图片到本地
            return saveBufferImageToLocalServer(mainImage, downloadUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("-1", "读取文件异常");
        }
    }

    /**
     * 调用模型合并图片接口，结果持久化到本地文件
     * @param multipartFile
     * @param bgKey
     * @param apiUrl
     * @return 可访问的网站文件链接
     */
    @Override
    public FileRecordEntity getMergedPhotoUrl(MultipartFile multipartFile, String bgKey, String apiUrl, String downloadUrl){
        if(StringUtils.isEmpty(bgKey) || multipartFile.isEmpty()) {
            throw new BusinessException("-1", "参数错误，背景图或原图为空");
        }
        try {
            // 主要图片
            BufferedImage mainImage = ImageIO.read(multipartFile.getInputStream());
            // 获取上传图片尺寸
            log.info("上传图片的尺寸：with = " + mainImage.getWidth() + " height = " + mainImage.getHeight());
            String backgroundBase64 = (String) baseCache.getTenHoursCache().get(bgKey, () -> {
                String localPhotoUrl = "static/" + bgKey;
                // 获取静态 logo 图片
                ClassPathResource classPathResource = new ClassPathResource(localPhotoUrl);
                // Base64 编码
                InputStream inputStream =classPathResource.getInputStream();
                String base64Str = Base64Util.inputStreamToBase64Str(inputStream);
                // 删除 \r\n
                base64Str = base64Str.replaceAll("[\\s*\t\n\r]", "");
                // 删除 前缀
                base64Str =  base64Str.contains(",") ? base64Str.split(",")[1] : base64Str;
                return base64Str;
            });
            // Base64 编码
            String photoBase64 = Base64Util.inputStreamToBase64Str(multipartFile.getInputStream());
            // 删除 \r\n
            photoBase64 = photoBase64.replaceAll("[\\s*\t\n\r]", "");
            // 删除前缀
            photoBase64 = photoBase64.contains(",") ? photoBase64.split(",")[1] : photoBase64;
            // 获取模型合并结果
            log.info("========== 开始调用图片融合接口");
            long startTime = System.currentTimeMillis();
            String mergeResult = getMergeResult(photoBase64, backgroundBase64, apiUrl);
            log.info("========== 图片融合接口完毕 用时：" + (System.currentTimeMillis() - startTime) + "ms");
            // 获取可访问的文件 url
            if(null == mergeResult || mergeResult.contains("error")) {
                throw new BusinessException("-1", "模块返回出错");
            }
            return saveBase64FileToLocalServer(mergeResult, downloadUrl);
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
        // 构造请求体
        ArrayList<String> requestData = new ArrayList<>();
        requestData.add(photoBase64);
        requestData.add(backgroundBase64);
        // 发送请求
        String mergeResult;
        try {
            mergeResult = SysHttpUtils.getInstance().sendJsonPost(apiUrl, JSON.toJSONString(requestData));
            return mergeResult;
        } catch (Exception e) {
            throw new BusinessException("-1", "连接模型服务器失败，请稍后再试");
        }
    }

    private FileRecordEntity saveBufferImageToLocalServer(BufferedImage image, String downloadUrl) {
        // 生成本地文件
        try {
            String fileRootDir = generateFileRootDir() + File.separator;
            String fileName = SysRandomUtil.getRandomString(16) + ".png";
            File newFile = new File(fileRootDir + fileName);
            ImageIO.write(image, "png", newFile);
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setFileName(fileName);
            fileRecord.setLocalAddress(fileRootDir + fileName);
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            // 保存所有文件记录
            fileRecordDao.save(fileRecord);
            fileRecord.setFileUrl(downloadUrl + "/" + fileRecord.getId());
            fileRecordDao.save(fileRecord);
            return fileRecord;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("-1", "图片写入到本地失败");
        }
    }

    private FileRecordEntity saveBase64FileToLocalServer(String resultStr, String downloadUrl) {
        // 判断传参
        if(StringUtils.isEmpty(resultStr) || !resultStr.contains("base64")) {
            throw new BusinessException("-1", "模型接口返回为空");
        }
        // jsonStr -> JsonObject
        JSONObject mergedObj = JSON.parseObject(resultStr);
        String base64Str = mergedObj.getString("base64");
        // 生成本地文件
        String fileRootDir = generateFileRootDir() + File.separator;
        String fileName = SysRandomUtil.getRandomString(16) + ".jpg";
        // 保存到本地
        try {
            Base64Util.base64ContentToFile(base64Str, fileRootDir + fileName);
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setFileName(fileName);
            fileRecord.setLocalAddress(fileRootDir + fileName);
            fileRecord.setUploadTime(new Timestamp(System.currentTimeMillis()));
            // 保存所有文件记录
            fileRecordDao.save(fileRecord);
            fileRecord.setFileUrl(downloadUrl + "/" + fileRecord.getId());
            fileRecordDao.save(fileRecord);
            return fileRecord;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("-1", "文件保存到本地失败");
        }
    }
}
