package com.lzx.aspect;

import com.alibaba.fastjson.JSON;
import com.lzx.annotation.SystemLog;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    //声明公共的切入点
    @Pointcut("@annotation(com.lzx.annotation.SystemLog)")  //使用注解的方式，自定义的注解加到那里，哪里就打印日志
    public void pointCut() {
    }
    @Around("pointCut()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            handlerBefore(joinPoint);
            result = joinPoint.proceed();
            handlerAfter(result);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return result;
    }

    private void handlerAfter(Object result) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(result));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {  //获取一个方法上的注解对象
        MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(SystemLog.class);
    }

    private void handlerBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();  //获取reques
        //获取被增强方法上的注解对象
        SystemLog systemLog=getSystemLog(joinPoint);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }
}
