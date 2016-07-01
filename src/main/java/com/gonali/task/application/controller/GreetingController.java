package com.gonali.task.application.controller;

import com.alibaba.fastjson.JSON;
import com.gonali.task.dao.TaskModelDao;
import com.gonali.task.dao.TaskSlaveModelDao;
import com.gonali.task.model.EntityModel;
import com.gonali.task.model.TaskModel;
import com.gonali.task.model.TaskSlaveModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@Controller
public class GreetingController {

  //  @Autowired
   // JdbcTemplate jdbcTemplate;


//    TaskSlaveModelDao dao = new TaskSlaveModelDao();

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {

      //  TaskModelDao dao = new TaskModelDao();

      //  List<EntityModel> taskModelList = dao.selectAll("crawlerTaskTable");
//        TaskSlaveModel tm = new TaskSlaveModel();
//        tm.setSlaveId("123456");
//        tm.setSlaveIp("192.168.1.10");
//        tm.setSlaveSshPort(223);
//        tm.setSlaveUsername("user");
//        tm.setSlavePassword("passwd");
//        dao.insert("crawlerTaskSlaveTable", tm);
//        model.addAttribute("name", name);
      //  DataSource ds = jdbcTemplate.getDataSource();
        model.addAttribute("tasks" , "hello!!!");
        return "greeting";
    }

}
