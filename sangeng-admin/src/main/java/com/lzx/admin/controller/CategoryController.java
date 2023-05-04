package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.CategoryDto;
import com.lzx.domain.service.CategoryService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")  //当前用户是否有导出权限
    @GetMapping("/export")
    public void categoryExport(HttpServletResponse response){
        categoryService.export(response);
    }
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String name,String status){
        return categoryService.list(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult addCategory(@RequestBody Map<String,String> map){
        String name = map.get("name");
        String description = map.get("description");
        String status = map.get("status");
        return categoryService.addCategory(name,description,status);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Long id){
        return categoryService.getCategoryById(id);
    }
    @PutMapping
    public ResponseResult getCategoryById(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);

    }

}
