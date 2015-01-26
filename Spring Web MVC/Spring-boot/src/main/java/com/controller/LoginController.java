package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping
    @Transactional(readOnly = true)
    public String home(){
        return "hello";
    }
}
