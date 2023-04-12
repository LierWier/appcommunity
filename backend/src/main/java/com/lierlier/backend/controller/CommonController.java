package com.lierlier.backend.controller;

import com.lierlier.backend.utils.FileUploadUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    @GetMapping("/upload/**")
    public ResponseEntity<Resource> getImage(HttpServletRequest request) throws IOException {
        String requestUri = request.getRequestURI();
        String imagePath = requestUri.substring(18);
        // 根据图片路径创建 Resource 对象
        Resource resource = new FileSystemResource("uploads/" + imagePath);
        // 如果资源文件不存在，返回 404
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        // 返回 ResponseEntity 对象
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PostMapping("/upload/cache")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        // 生成新的文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = DigestUtils.sha256Hex(file.getBytes()) + fileExtension;

        try {
            FileUploadUtils.saveFile(fileName, file, "cache");
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + fileName + "!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to upload " + fileName + "!");
        }
        return "http://127.0.0.1:3001/api/common/upload/cache/" + fileName;
    }
}
