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
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello("jack" + i));
        }

    }
}
