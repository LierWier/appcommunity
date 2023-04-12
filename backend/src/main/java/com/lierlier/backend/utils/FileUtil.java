package com.lierlier.backend.utils;

import java.io.File;

public class FileUtil {
    public static boolean deleteFile(String filePath) {
        File file = new File("uploads" + filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String urlToPath(String url) {
        int startIndex = url.indexOf("/upload");
        if (startIndex == -1) {
            return null; // 如果 URL 不包含 "/upload/"，返回 null 表示无法转成路径
        }
        return url.substring(startIndex + 7);
    }

}
