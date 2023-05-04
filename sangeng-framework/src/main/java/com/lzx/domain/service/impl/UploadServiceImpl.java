package com.lzx.domain.service.impl;

import com.google.gson.Gson;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.service.UploadService;
import com.lzx.exception.SystemException;
import com.lzx.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;
    @Override
    public ResponseResult uploadImg(MultipartFile headerImg) {
        if(headerImg.isEmpty()){
            throw new RuntimeException("上传头像为空");
        }
        //获取原始文件名
        String filename = headerImg.getOriginalFilename();
        //对原始文件名进行判断,判断文件类型
        if(!(filename.endsWith(".jpg")||filename.endsWith(".png"))){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //判断通过上传文件到oss
        String filePath = PathUtils.generateFilePath(filename);
        String url = uploadOSS(headerImg, filePath);
        return ResponseResult.okResult(url);
    }

    @Override
    public String uploadOSS(MultipartFile headerImg, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream inputStream = headerImg.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rr4rna0ku.hd-bkt.clouddn.com/"+filePath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "error";
    }
}
