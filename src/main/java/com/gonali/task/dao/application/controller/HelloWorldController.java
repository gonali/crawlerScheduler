package com.gonali.task.dao.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by TianyuanPan on 6/30/16.
 */
@Controller
public class HelloWorldController {

    @RequestMapping("/hello")
    public String helloWorld(){

        return "greeting";
    }
}
