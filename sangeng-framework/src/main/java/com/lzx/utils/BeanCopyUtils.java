package com.lzx.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
public class BeanCopyUtils {
    private BeanCopyUtils(){

    }
    public static <O,V>List<V> copyBeanList(List<O> src,Class<V> clazz){
        //方式一：
//        List<V> res=new ArrayList<>();
//        for(Object o : src){
//            V v = copyBean(o, clazz);
//            res.add(v);
//        }
//        return res;
        //方式二 :使用函数式编程
        return src.stream()  //转换成流
                .map(o->copyBean(o,clazz)) //对流中的每个元素进行操作
                .collect(Collectors.toList()); //收集结果

    }
    public static <V> V copyBean(Object src,Class<V> clazz){
        //创建目标对象
        try {
            V tar = clazz.newInstance();
            //实现属性拷贝
            BeanUtils.copyProperties(src,tar);
            //返回结果
            return tar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
