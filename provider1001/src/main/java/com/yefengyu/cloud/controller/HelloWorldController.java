package com.yefengyu.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController
{
    @GetMapping("/hello")
    public String hello()
    {
        return "hello spring cloud, 1001";
    }
}
