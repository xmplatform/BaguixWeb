/*
 * Copyright(c) www.baguix.com, All Rights Reserved!
 * Created by BaguixStudio on 2015/8/23.
 */
package com.baguix.utils.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <b>类的说明</b><br>
 *
 * @author Scott(SG)
 * @since 1.0
 */
public class FileTypeTool {
    // 单例模式
    private static FileTypeTool instance;
    private static FileTypeTool getInstance(){
        if(instance == null){
            instance = new FileTypeTool();
        }
        return instance;
    }

    // 隐藏构造函数
    private FileTypeTool(){

    }
    /**
     * <b>判断文件类型</b>
     * @param file
     * @return
     * @throws IOException
     */
    public static FileType getType(String file) throws IOException {
        if(file!=null && !"".equals(file)) {
            String fileHead = getFileContent(file);
            if (fileHead == null || fileHead.length() == 0) {
                return null;
            }
            fileHead = fileHead.toUpperCase();
            FileType[] fileTypes = FileType.values();
            for (FileType type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type;
                }
            }
        }
        return null;
    }

    /**
     * 将文件头转换成16进制字符串
     *
     * @param src 字符串字节
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     *
     * @param filePath 文件路径
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(String filePath) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }
}