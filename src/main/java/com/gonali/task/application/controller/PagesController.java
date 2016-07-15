package com.gonali.task.application.controller;

import com.gonali.task.utils.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by TianyuanPan on 7/6/16.
 */
@Controller
@RequestMapping("/")
public class PagesController {

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) {

        try {
            if (!SessionUtils.isValidSession(request))
                response.sendRedirect("/login.html");
            else
                response.sendRedirect("/index.html");
        } catch (IOException e) {

            e.printStackTrace();
        }


    }
}
