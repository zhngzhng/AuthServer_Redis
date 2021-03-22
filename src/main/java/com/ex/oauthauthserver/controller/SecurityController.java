package com.ex.oauthauthserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//使用restController可以使thymeleaf返回失效
@Controller
public class SecurityController {
    @RequestMapping("/login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/register")
    public String toRegister(){
        return "register";
    }
    @RequestMapping("/resetPwd")
    public String toRestPwd(){
        return "resetPwdPage";
    }


    //测试
    @RequestMapping(value = "/user/test",method = RequestMethod.GET)
    public String toTest(){
        return "loginTest";
    }
}
