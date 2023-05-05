package com.lzx.blog;

import com.lzx.domain.mapper.AdminGetInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootTest(classes = BlogApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test {
    @Autowired
    private AdminGetInfoMapper adminGetInfoMapper;
    @org.junit.jupiter.api.Test
    public void test(){
        List<String> permissions = adminGetInfoMapper.permissions(1L);
        System.out.println(permissions);
    }
}
