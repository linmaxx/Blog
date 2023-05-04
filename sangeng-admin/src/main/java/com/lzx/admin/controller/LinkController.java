package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.LinkDto;
import com.lzx.domain.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Map;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String name,String status){
       return linkService.list(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody LinkDto linkDto){
        return linkService.addLink(linkDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id") Long id){
        return linkService.getLinkById(id);
    }
    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        return linkService.updateLink(linkDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult deleteLink(@PathVariable("ids") List<Long> ids){
        return linkService.deleteLink(ids);
    }
    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody Map<String,String> map){

        return linkService.changeLinkStatus(Long.valueOf(map.get("id")),map.get("status"));
    }

}
