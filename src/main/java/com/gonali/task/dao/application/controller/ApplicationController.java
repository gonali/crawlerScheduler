package com.gonali.task.dao.application.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gonali.task.dao.application.model.ResponseStatus;
import com.gonali.task.dao.config.Config;
import com.gonali.task.dao.dao.TaskSlaveModelDao;
import com.gonali.task.dao.message.RuntimeControlMsg;
import com.gonali.task.dao.model.EntityModel;
import com.gonali.task.dao.model.HeartbeatMsgModel;
import com.gonali.task.dao.model.TaskModel;
import com.gonali.task.dao.scheduler.TaskScheduler;
import com.gonali.task.dao.utils.MD5Utils;
import com.gonali.task.dao.utils.SessionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 7/6/16.
 */
@RestController
@RequestMapping("/api")
public class ApplicationController {

    private static RuntimeControlMsg runtimeControlMsg;
    private static TaskScheduler scheduler;
    private static Config config;

    static {

        runtimeControlMsg = RuntimeControlMsg.getRuntimeControlMsg();
        scheduler = TaskScheduler.createTaskScheduler();
        config = Config.getConfig();
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
    public String logout(HttpServletRequest request) {

        SessionUtils.removeSession(request);

        return new ResponseStatus().setStatus(true).toString();
    }

    @RequestMapping("unloginproccess")
    public String unloginproccess(HttpServletRequest request) {

        return new ResponseStatus().setStatus(false)
                .setLocation("login.html")
                .setIsRedirect(true).toString();
    }

    @RequestMapping("taskStatus")
    public String taskStatus(HttpServletRequest request) {

        return "{status}";
    }


    @RequestMapping("slaveInfo")
    public String slaveInfo() {

        try {
            List<EntityModel> slaveList = new TaskSlaveModelDao().selectAll(config.getTaskSlaveTable());

            return JSON.toJSONString(slaveList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /***
     * @return json
     * <p/>
     * date format
     * <p/>
     * [
     * {
     * "taskId":"String",
     * "taskName":"String",
     * "heartbeatNumber":int,
     * "startTime":"YYYY-MM-dd HH:mm:ss",
     * "costTime":"String"
     * },
     * ... ...
     * ]
     */
    @RequestMapping("getCurrentTask")
    public String getCurrentTask() {

        class CurrentTaskView {

            private String taskId;
            private String taskName;
            private int heartbeatNumber;
            private String startTime;
            private String costTime;

            public CurrentTaskView() {
            }

            public CurrentTaskView(String taskId, String taskName,
                                   int heartbeatNumber, String startTime,
                                   String costTime) {

                this.taskId = taskId;
                this.taskName = taskName;
                this.heartbeatNumber = heartbeatNumber;
                this.startTime = startTime;
                this.costTime = costTime;
            }

            public String getTaskId() {
                return taskId;
            }

            public void setTaskId(String taskId) {
                this.taskId = taskId;
            }

            public String getTaskName() {
                return taskName;
            }

            public void setTaskName(String taskName) {
                this.taskName = taskName;
            }

            public int getHeartbeatNumber() {
                return heartbeatNumber;
            }

            public void setHeartbeatNumber(int heartbeatNumber) {
                this.heartbeatNumber = heartbeatNumber;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getCostTime() {
                return costTime;
            }

            public void setCostTime(String costTime) {
                this.costTime = costTime;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        List<TaskModel> taskModelList = scheduler.getCurrentTasks().getCurrentTaskElements();
        List<CurrentTaskView> taskViews = new ArrayList<>();
        int size = taskModelList.size();

        for (int i = 0; i < size; i++) {

            String taskId = taskModelList.get(i).getTaskId();
            String taskName = taskModelList.get(i).getTaskName();
            int heartbeatNumber = scheduler.getCurrentTasks().getHeartbeatList(taskId).size();
            String startTime = dateFormat.format(new Date(taskModelList.get(i).getTaskStartTime()));
            int deta = (int) (System.currentTimeMillis() - taskModelList.get(i).getTaskStartTime());
            String costTime = "" + (deta / 1000 / 60 / 60) + " h " + (deta / 1000 / 60 % 60) + " m " + ((deta / 1000 % 60)) + " s";
            taskViews.add(new CurrentTaskView(taskId, taskName, heartbeatNumber, startTime, costTime));

        }

        return JSON.toJSONString(taskViews);

    }


    @RequestMapping("getPidAndTaskState")
    public String getPidAndTaskState() {

        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        boolean state = scheduler.getRuntimeControlMsg().isTaskRunning();
        boolean wait = scheduler.getRuntimeControlMsg().isCurrentTasksFinished();

        return "{\"pid\":\"" + pid + "\",\"state\":" + state + ",\"wait\":" + wait + "}";
    }

    @RequestMapping("getQueueAndTaskNumber")
    public String getQueueAndTask() {

        int maxTaskRun = config.getTaskConfig().getMaxTaskRun();
        int maxQueue = config.getTaskConfig().getMaxTaskQueueSize();
        int currentQueue = (int) scheduler.getRuler().getCurrentTaskQueueLength();

        return "{\"maxTaskRun\":" + maxTaskRun + ",\"maxQueue\":" + maxQueue + ",\"currentQueue\":" + currentQueue + "}";

    }


    @RequestMapping("getInQueueTask")
    public String getInQueueTask() {

        String[][] inQueueTask = scheduler.getRuler().getInQueueTaskIdAndNameArray();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        try {
            for (int i = 0; i < inQueueTask.length; i++) {

                if (inQueueTask[i][0] != null) {

                    jsonObject = new JSONObject();

                    jsonObject.put("taskId", inQueueTask[i][0]);
                    jsonObject.put("taskName", inQueueTask[i][1]);
                    jsonObject.put("enQueueTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(Long.parseLong(inQueueTask[i][2]))));

                    jsonArray.add(jsonObject);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return jsonArray.toJSONString();
    }


    @RequestMapping("getHeartbeatInfo")
    public String getHeartbeatInfo(){

        List<HeartbeatMsgModel> heartbeatMsgModelList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        for (HeartbeatMsgModel h : heartbeatMsgModelList){

            jsonObject = new JSONObject();
            jsonObject.put("taskId", h.getTaskId());
            jsonObject.put("host", h.getHostname());
            jsonObject.put("pid", h.getPid());
            jsonObject.put("threads", h.getThreads());
            jsonObject.put("statusCode", h.getStatusCode());
            jsonObject.put("timeoutCount", h.getTimeoutCount());
            jsonObject.put("time", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(h.getTime())));

            jsonArray.add(jsonObject);
        }

        return jsonArray.toJSONString();
    }



}
