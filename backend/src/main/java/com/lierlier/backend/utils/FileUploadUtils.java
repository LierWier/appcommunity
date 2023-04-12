package com.lierlier.backend.utils;

import com.lierlier.backend.Constant;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadUtils {
    public static void saveFile(String fileName, MultipartFile file, String dir) throws IOException {
        File uploadDir = new File("uploads/" + dir);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File dest = new File(uploadDir.getAbsolutePath() + File.separator + fileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
    }

    public static String saveFile(String fileName, String fileUrl, String dir) throws IOException {
        URL url = new URL(fileUrl);
        byte[] imageData = IOUtils.toByteArray(url);
        // 保存文件到磁盘
        Path uploadDir = Paths.get("uploads/" + dir);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(fileName);
        Files.write(filePath, imageData);
        return Constant.localhostUrl + "api/common/upload/" + dir + fileName;
    }
}
