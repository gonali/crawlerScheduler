package com.gonali.task.dao.application.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gonali.task.dao.application.model.ResponseStatus;
import com.gonali.task.dao.config.Config;
import com.gonali.task.dao.dao.TaskConfigModelDao;
import com.gonali.task.dao.dao.TaskModelDao;
import com.gonali.task.dao.dao.TaskSlaveModelDao;
import com.gonali.task.dao.message.RuntimeControlMsg;
import com.gonali.task.dao.model.*;
import com.gonali.task.dao.scheduler.TaskScheduler;
import com.gonali.task.dao.utils.MD5Utils;
import com.gonali.task.dao.utils.RandomUtils;
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


    @RequestMapping("startScheduler")
    public String startScheduler() {
        runtimeControlMsg.setSchedulerState(true);
        return new ResponseStatus()
                .setStatus(true)
                .toString();
    }


    @RequestMapping("stopScheduler")
    public String stopScheduler() {
        runtimeControlMsg.setIsTaskRunning(false);
        return new ResponseStatus()
                .setStatus(true)
                .toString();
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
        boolean wait = !scheduler.getRuntimeControlMsg().isCurrentTasksFinished();

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
    public String getHeartbeatInfo() {

        List<HeartbeatMsgModel> heartbeatMsgModelList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        for (HeartbeatMsgModel h : heartbeatMsgModelList) {

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


    @RequestMapping("getRuntimeControlMsg")
    public String getRuntimeControlMsg() {

        try {
            return JSON.toJSONString(scheduler.getRuntimeControlMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * @return json string
     * {
     * "adminPassword":"21232f297a57a5a743894a0e4a801fc3",
     * "configId":1,
     * "maxHeartbeatTimeoutCount":30,
     * "maxTaskQueueSize":10,
     * "maxTaskRun":3,
     * "primaryKey":"1",
     * "redisHost":"110.110.10.100",
     * "redisPort":6379,
     * "slaveAppScript":"./crawlerStart.sh",
     * "slaveHeartbeatInterval":10
     * }
     */
    @RequestMapping("getSchedulerConfig")
    public String getSchedulerConfig() {

        try {
            EntityModel configModel = new TaskConfigModelDao().selectAll(config.getTaskConfigTable()).get(0);
            return JSON.toJSONString(configModel);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return "{}";
    }

    @RequestMapping("getAllTaskShortcut")
    public String getAllTaskShortcut() {

        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;

            List<EntityModel> taskList = new TaskModelDao().selectAll(config.getTaskTable());
            int size = taskList.size();
            for (int i = 0; i < size; i++) {

                jsonObject = new JSONObject();
                jsonObject.put("taskId", ((TaskModel) taskList.get(i)).getTaskId());
                jsonObject.put("userId", ((TaskModel) taskList.get(i)).getUserId());
                jsonObject.put("taskName", ((TaskModel) taskList.get(i)).getTaskName());
                jsonObject.put("taskType", ((TaskModel) taskList.get(i)).getTaskType());
                jsonObject.put("taskRemark", ((TaskModel) taskList.get(i)).getTaskRemark());

                jsonArray.add(jsonObject);

            }
            return jsonArray.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "[]";
    }

    /**
     * @return json string
     * json format
     * <p/>
     * [
     * {
     * "slaveAppPath":"~/",
     * "slaveId":"1",
     * "slaveIp":"110.110.10.101",
     * "slavePassword":"crawler",
     * "slaveSshPort":22,
     * "slaveUsername":"crawler"
     * },
     * ... ...
     * ]
     */
    @RequestMapping("getAllSlaveInfo")
    public String getAllSlaveInfo() {

        try {
            List<EntityModel> slaveList = new TaskSlaveModelDao().selectAll(config.getTaskSlaveTable());

            return JSON.toJSONString(slaveList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("getEditSlave")
    public String getEditSlave(HttpServletRequest request) {

        try {
            String slaveId = request.getParameter("slaveId");
            TaskSlaveModelDao dao = new TaskSlaveModelDao();
            EntityModel slave = dao.selectById(config.getTaskSlaveTable(), slaveId);
            return JSON.toJSONString(slave);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{}";
    }


    @RequestMapping("deleteSlaveById")
    public String deleteSlaveById(HttpServletRequest request) {

        try {
            String slaveId = request.getParameter("slaveId");
            if (slaveId == null)
                return new ResponseStatus().setStatus(false).toString();

            TaskSlaveModelDao dao = new TaskSlaveModelDao();

            if(dao.deleteById(config.getTaskSlaveTable(), slaveId) > 0)
                return new ResponseStatus().setStatus(true).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseStatus().setStatus(false).toString();
    }

    @RequestMapping("addSlave")
    public String addSlave(HttpServletRequest request){
        try {
            TaskSlaveModel slave = new TaskSlaveModel();
            String slaveId = RandomUtils.getRandomString(10);
            String slaveIp = request.getParameter("slaveIp");
            String slaveSshPort = request.getParameter("slaveSshPort");
            String slaveUsername = request.getParameter("slaveUsername");
            String slavePassword = request.getParameter("slavePassword");
            String slaveAppPath = request.getParameter("slaveAppPath");

            if (slaveId == null) {

                return new ResponseStatus()
                        .setStatus(false)
                        .setIsRedirect(true)
                        .setLocation("index.html")
                        .toString();
            }

            slave.setSlaveId(slaveId);
            slave.setSlaveIp(slaveIp);
            slave.setSlaveUsername(slaveUsername);
            slave.setSlavePassword(slavePassword);
            slave.setSlaveSshPort(Integer.parseInt(slaveSshPort));
            slave.setSlaveAppPath(slaveAppPath);

            TaskSlaveModelDao dao = new TaskSlaveModelDao();

            int ret = dao.insert(config.getTaskSlaveTable(), slave);
            if (ret > 0)
                return new ResponseStatus()
                        .setStatus(true)
                        .toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseStatus()
                .setStatus(false)
                .setIsRedirect(true)
                .setLocation("index.html")
                .toString();
    }

    @RequestMapping("updateSlave")
    public String updateSlave(HttpServletRequest request) {

        try {
            TaskSlaveModel slave = new TaskSlaveModel();
            String slaveId = request.getParameter("slaveId");
            String slaveIp = request.getParameter("slaveIp");
            String slaveSshPort = request.getParameter("slaveSshPort");
            String slaveUsername = request.getParameter("slaveUsername");
            String slavePassword = request.getParameter("slavePassword");
            String slaveAppPath = request.getParameter("slaveAppPath");

            if (slaveId == null) {

                return new ResponseStatus()
                        .setStatus(false)
                        .setIsRedirect(true)
                        .setLocation("index.html")
                        .toString();
            }

            slave.setSlaveId(slaveId);
            slave.setSlaveIp(slaveIp);
            slave.setSlaveUsername(slaveUsername);
            slave.setSlavePassword(slavePassword);
            slave.setSlaveSshPort(Integer.parseInt(slaveSshPort));
            slave.setSlaveAppPath(slaveAppPath);

            TaskSlaveModelDao dao = new TaskSlaveModelDao();

            int ret = dao.update(config.getTaskSlaveTable(), slave);
            if (ret > 0)
                return new ResponseStatus()
                        .setStatus(true)
                        .toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseStatus()
                .setStatus(false)
                .setIsRedirect(true)
                .setLocation("index.html")
                .toString();
    }


    @RequestMapping("updateSchedulerConfig")
    public String updateSchedulerConfig(HttpServletRequest request) {

        try {
            String configId = request.getParameter("configId");
            String redisHost = request.getParameter("redisHost");
            String redisPort = request.getParameter("redisPort");
            String maxTaskQueueSize = request.getParameter("maxTaskQueueSize");
            String maxTaskRun = request.getParameter("maxTaskRun");
            String maxHeartbeatTimeoutCount = request.getParameter("maxHeartbeatTimeoutCount");
            String slaveHeartbeatInterval = request.getParameter("slaveHeartbeatInterval");
            String slaveAppScript = request.getParameter("slaveAppScript");
            String adminPassword = request.getParameter("adminPassword");

            if (configId == null)
                return new ResponseStatus()
                        .setStatus(false)
                        .setIsRedirect(false)
                        .toString();

            TaskConfigModel model = new TaskConfigModel();
            TaskSlaveModelDao dao = new TaskSlaveModelDao();

            model.setConfigId(Integer.parseInt(configId));
            model.setRedisHost(redisHost);
            model.setRedisPort(Integer.parseInt(redisPort));
            model.setMaxTaskQueueSize(Integer.parseInt(maxTaskQueueSize));
            model.setMaxTaskRun(Integer.parseInt(maxTaskRun));
            model.setMaxHeartbeatTimeoutCount(Integer.parseInt(maxHeartbeatTimeoutCount));
            model.setSlaveHeartbeatInterval(Integer.parseInt(slaveHeartbeatInterval));
            model.setSlaveAppScript(slaveAppScript);

            if (!SessionUtils.getPassword().equals(adminPassword))
                model.setAdminPassword(MD5Utils.getStringMD5(adminPassword));
            else model.setAdminPassword(SessionUtils.getPassword());

            if (dao.update(config.getTaskConfigTable(), model) > 0)
                return new ResponseStatus()
                        .setStatus(true)
                        .setIsRedirect(false)
                        .toString();
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }


        return new ResponseStatus()
                .setStatus(false)
                .setIsRedirect(false)
                .toString();
    }

}
