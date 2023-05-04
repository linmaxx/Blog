package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.Menu;
import com.lzx.domain.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult list(String status,String menuName){
        return menuService.adminMenuList(status,menuName);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    @GetMapping("/{id}")
    public ResponseResult getMenuDetail(@PathVariable("id") long id){
        return menuService.getMenuDetail(id);
    }
    @PutMapping
    public ResponseResult adminUpdateMenu(@RequestBody Menu menu){
        return menuService.adminUpdateMenu(menu);
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult adminDeleteMenu(@PathVariable("menuId") long menuId){
        return menuService.adminDeleteMenu(menuId);
    }
}
