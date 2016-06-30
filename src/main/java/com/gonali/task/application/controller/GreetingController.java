package com.gonali.task.application.controller;

import com.alibaba.fastjson.JSON;
import com.gonali.task.dao.TaskModelDao;
import com.gonali.task.model.EntityModel;
import com.gonali.task.model.TaskModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@Controller
public class GreetingController {

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {

      //  TaskModelDao dao = new TaskModelDao();

      //  List<EntityModel> taskModelList = dao.selectAll("crawlerTaskTable");

        model.addAttribute("name", name);
       // model.addAttribute("tasks" , JSON.toJSONString(taskModelList));
        return "greeting";
    }

}
