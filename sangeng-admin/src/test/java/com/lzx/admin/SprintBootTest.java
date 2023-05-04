package com.lzx.admin;

import com.lzx.domain.entity.Menu;
import com.lzx.domain.entity.Tag;
import com.lzx.domain.mapper.MenuMapper;
import com.lzx.domain.mapper.TagMapper;
import com.lzx.domain.vo.AdminAddRoleTreeVo;
import com.lzx.domain.vo.TransferVo;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootTest
public class SprintBootTest {
    @Autowired
    TagMapper tagMapper;
    @Autowired
    RedisCache redisCache;
    @Autowired
    MenuMapper menuMapper;
    @Test
    public void test(){
        //先查询菜单表获取所有行结果
        List<Menu> menus = menuMapper.selectList(null);
        List<TransferVo> transferVos = new ArrayList<>();
        for (Menu menu : menus){
            TransferVo transferVo = BeanCopyUtils.copyBean(menu, TransferVo.class);
            transferVos.add(transferVo);
        }
        System.out.println(transferVos);
        transferVos.stream()
                .forEach(transferVo -> transferVo.setMenuName(transferVo.getLabel()));
        List<AdminAddRoleTreeVo> adminAddRoleTreeVos = BeanCopyUtils.copyBeanList(transferVos, AdminAddRoleTreeVo.class);

    }


}
