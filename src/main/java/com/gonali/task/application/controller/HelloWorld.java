package com.gonali.task.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by TianyuanPan on 6/30/16.
 */
@Controller
@RequestMapping("/")
public class HelloWorld {

    @RequestMapping("/")
    public String helloWorld(){

        return "greeting";
    }
}
