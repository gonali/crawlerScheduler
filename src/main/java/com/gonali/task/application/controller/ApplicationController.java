package com.gonali.task.application.controller;

import com.gonali.task.application.model.ResponseStatus;
import com.gonali.task.message.RuntimeControlMsg;
import com.gonali.task.scheduler.TaskScheduler;
import com.gonali.task.utils.MD5Utils;
import com.gonali.task.utils.SessionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by TianyuanPan on 7/6/16.
 */
@RestController
@RequestMapping("/api")
public class ApplicationController {

    private static RuntimeControlMsg runtimeControlMsg;
    private static TaskScheduler scheduler;

    static {

        runtimeControlMsg = RuntimeControlMsg.getRuntimeControlMsg();
        scheduler = TaskScheduler.createTaskScheduler();
    }

    @RequestMapping("login")
    public String login(HttpServletRequest request) {
        ResponseStatus status = new ResponseStatus();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            return status.setStatus(false).toString();
        }
        password = MD5Utils.getStringMD5(password);

        if (username.equals(SessionUtils.getUsername()) && password.equals(SessionUtils.getPassword())) {
            SessionUtils.setSession(request);

            return status.setStatus(true).toString();
        } else {

            return status.setStatus(false).toString();
        }

    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request){

        SessionUtils.removeSession(request);

        return new ResponseStatus().setStatus(true).toString();
    }

    @RequestMapping("unloginproccess")
    public String unloginproccess(HttpServletRequest request){

        return new ResponseStatus().setStatus(false)
                .setLocation("/api/login")
                .setIsRedirect(true).toString();
    }

    @RequestMapping("taskStatus")
    public String taskStatus(HttpServletRequest request) {

        return "{status}";
    }

}
