package com.blue.girl.server.utils;


import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class Base64Util {

    // Base64 编码与解码
    private static final Base64.Decoder DECODER_64 = Base64.getDecoder();
    private static final Base64.Encoder ENCODER_64 = Base64.getEncoder();

    // dpi越大转换后的图片越清晰，相对转换速度越慢
    private static final Integer DPI = 100;

    // 编码、解码格式
    private static final String CODE_FORMATE = "UTF-8";

    /**
     * 1. text明文 转 Base64字符串
     * @param text  明文
     * @return      Base64 字符串
     */
    public static String textToBase64Str(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String encodedToStr = null;
        try {
            encodedToStr = ENCODER_64.encodeToString(text.getBytes(CODE_FORMATE));
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        return encodedToStr;
    }

    /**
     * 2. text 的 Base64 字符串转明文
     * @param base64Str text 的 Base64 字符串
     * @return text明文
     */
    public static String base64StrToText(String base64Str) {
        if (StringUtils.isBlank(base64Str)) {
            return base64Str;
        }
        String byteToText = null;
        try {
            byteToText = new String(DECODER_64.decode(base64Str), CODE_FORMATE);
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        return byteToText;
    }

    /**
     * 3. 文件（图片、pdf） 转 Base64 字符串
     * @param file  需要转Base64的文件
     * @return      Base64 字符串
     */
    public static String fileToBase64Str(File file) throws IOException {
        String base64Str = null;
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout = null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            // io
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while(len != -1){
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            // 刷新此输出流，强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            // Base64字符编码
            base64Str = ENCODER_64.encodeToString(bytes).trim();
        } catch (IOException e) {
            e.getMessage();
        } finally{
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return base64Str;
    }

    /**
     * inputStream 转 base64Str
     * @param in
     * @return
     */
    public static String inputStreamToBase64Str(InputStream in) {
        byte[] data = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ENCODER_64.encodeToString(data);
    }

    /**
     * 4. Base64字符串 转 文件（图片、pdf） -- 多用于测试
     * @param base64Content Base64 字符串
     * @param filePath      存放路径
     */
    public static void base64ContentToFile(String base64Content, String filePath) throws IOException{
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            // Base64解码到字符数组
            byte[] bytes =  DECODER_64.decode(base64Content);
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if(!path.exists()){
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            // io
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while(length != -1){
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            // 刷新此输出流，强制写出所有缓冲的输出字节
            bos.flush();
        } catch (IOException e) {
            e.getMessage();
        }finally {
            try {
                bis.close();
                fos.close();
                bos.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }


    // 测试
    public static void main(String args[]){
        // 1.测试：text明文 转 Base64字符串
        String text = "这是一串需要编码的明文，可以是URL、图片、文件或其他。";
        String result_1 = Base64Util.textToBase64Str(text);
        System.out.println("text明文 转 Base64字符串：" + text + " → 经Base64编码后 → " + result_1);

        // 2.测试：text的Base64字符串 转 明文
        String base64Str = "6L+Z5piv5LiA5Liy6ZyA6KaB57yW56CB55qE5piO5paH77yM5Y+v5Lul5pivVVJM44CB5Zu+54mH44CB5paH5Lu25oiW5YW25LuW44CC";
        String result_2 = Base64Util.base64StrToText(base64Str);
        System.out.println("text的Base64字符串 转 明文：" + base64Str + " → 经Base64解码后 → " + result_2);

        // 3.测试：文件 转 Base64
        // 4.测试：Base64 转 文件
        try {
            File filePath = new File("D:\\OCR\\test.pdf");
            String tempBase64Str = Base64Util.fileToBase64Str(filePath);
            System.out.println("文件 转 Base64，完成，使用方法【4】反转验证。");
            Base64Util.base64ContentToFile(tempBase64Str, "D:\\OCR\\test_4.pdf");
        } catch (IOException e) {
            e.getMessage();
        }
        System.out.println("文件与Base64互转，完成，方法【4】通常用于测试。");

    }
}
