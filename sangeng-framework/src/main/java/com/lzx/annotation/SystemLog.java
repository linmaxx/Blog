package com.lzx.annotation;

import org.aspectj.lang.annotation.Around;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)  //运行时生效
@Target({ElementType.METHOD}) //加在方法上
public @interface SystemLog {
    String businessName();
}
