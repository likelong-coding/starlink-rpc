package com.lkl.service;

import com.starlink.proxy.ServiceProxyFactory;

/**
 * @author likelong
 * @date 2024/12/21 17:56
 * @description
 */
public class TestService {
    public static void main(String[] args) {
        // 创建代理对象
        HelloService helloService = ServiceProxyFactory.getProxy(HelloService.class);
        String hello = helloService.hello("jack");
        helloService.hello("jack1");
        helloService.hello("jack2");
        System.out.println(hello);
    }
}
