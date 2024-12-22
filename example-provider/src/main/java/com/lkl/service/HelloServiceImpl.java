package com.lkl.service;

/**
 * @author likelong
 * @date 2024/12/21 17:06
 * @description
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
