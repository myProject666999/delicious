package com.delicious.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.delicious.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }
        
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = FileUtil.extName(originalFilename);
            String uuid = IdUtil.simpleUUID();
            String newFilename = uuid + "." + suffix;
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File destFile = new File(uploadPath + newFilename);
            file.transferTo(destFile);
            
            Map<String, String> result = new HashMap<>();
            result.put("url", "/api/upload/file/" + newFilename);
            result.put("filename", newFilename);
            
            return Result.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/file/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response) {
        File file = new File(uploadPath + filename);
        
        if (!file.exists()) {
            response.setStatus(404);
            return;
        }
        
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        
        try {
            FileUtil.writeToStream(file, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
