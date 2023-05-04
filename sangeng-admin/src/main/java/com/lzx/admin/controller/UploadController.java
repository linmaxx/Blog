package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult upload(@RequestPart("img") MultipartFile headerImg){
        return uploadService.uploadImg(headerImg);
    }
}
