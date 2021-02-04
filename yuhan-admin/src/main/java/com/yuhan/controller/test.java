package com.yuhan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author jzx
 * @Date 2021/2/2 16:47
 * @Version 1.0
 */
@RestController
public class test {

    @GetMapping("/test")
    public String test(){
        System.out.println("123456");
        return "123456";
    }
}
