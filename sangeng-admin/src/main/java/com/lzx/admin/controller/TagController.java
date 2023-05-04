package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.TagDto;
import com.lzx.domain.dto.TagListDto;
import com.lzx.domain.entity.Tag;
import com.lzx.domain.service.TagService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.PushBuilder;
import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.list(pageNum,pageSize,tagListDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);
    }
    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagDto){
        return tagService.addTag(tagDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult deleteTag( @PathVariable("ids") List<Long> ids){
        return tagService.deleteTag(ids);
    }
    @PutMapping
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        return tagService.updateTag(tagDto);
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}

