package com.lzx.domain.service;

import com.lzx.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile headerImg);
    String uploadOSS(MultipartFile headerImg,String filePath);
}
