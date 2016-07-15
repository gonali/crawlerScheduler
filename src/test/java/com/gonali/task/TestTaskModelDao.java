package com.gonali.task;

import com.alibaba.fastjson.JSON;
import com.gonali.task.dao.TaskModelDao;
import com.gonali.task.config.Config;
import com.gonali.task.dao.TaskModelDao;
import com.gonali.task.model.EntityModel;

import java.util.List;

/**
 * Created by TianyuanPan on 7/11/16.
 */
public class TestTaskModelDao {

    private  static TaskModelDao dao;
    private static Config config;

    public static void main(String[] args) {
        config = Config.getConfig();
        dao = new TaskModelDao();

        List<EntityModel> tasks = dao.selectAll(config.getTaskTable());

        System.out.println("Get data size: " + tasks.size());
        System.out.println("Data:\n" + JSON.toJSONString(tasks));
    }
}
