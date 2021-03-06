package com.gonali.task.application.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gonali.task.application.model.ResponseStatus;
import com.gonali.task.config.Config;
import com.gonali.task.dao.TaskConfigModelDao;
import com.gonali.task.dao.TaskModelDao;
import com.gonali.task.dao.TaskSlaveModelDao;
import com.gonali.task.message.RuntimeControlMsg;
import com.gonali.task.message.codes.TaskStatus;
import com.gonali.task.message.codes.TaskType;
import com.gonali.task.model.*;
import com.gonali.task.scheduler.TaskScheduler;
import com.gonali.task.utils.MD5Utils;
import com.gonali.task.utils.RandomUtils;
import com.gonali.task.utils.SessionUtils;
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

    private static TaskScheduler scheduler;
    private static RuntimeControlMsg runtimeControlMsg;
    private static Config config;

    static {

        scheduler = TaskScheduler.createTaskScheduler();
        runtimeControlMsg = scheduler.getRuntimeControlMsg();
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
        runtimeControlMsg.setIsTaskRunning(true);
        runtimeControlMsg.setIsHeartbeatUpdating(true);
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
        List<TaskModel> taskModelList = scheduler.getCurrentTasks().getCurrentUnfinishedTasks();
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

    @RequestMapping("addNewTask")
    public String addNewTask(HttpServletRequest request) {

        String taskId = request.getParameter("taskId");
        String userId = request.getParameter("userId");
        String taskName = request.getParameter("taskName");
        String taskType = request.getParameter("taskType");
        String taskStatus = request.getParameter("taskStatus");
        String taskRemark = request.getParameter("taskRemark");
        String taskSeedUrl = request.getParameter("taskSeedUrl");
        String taskCrawlerDepth = request.getParameter("taskCrawlerDepth");
        String taskPass = request.getParameter("taskPass");
        String taskNodeInstances = request.getParameter("taskNodeInstances");
        String taskInstanceThreads = request.getParameter("taskInstanceThreads");
        String taskWeight = request.getParameter("taskWeight");
        String taskStartTime = request.getParameter("taskStartTime");
        String taskStopTime = request.getParameter("taskStopTime");
        String taskRecrawlerDays = request.getParameter("taskRecrawlerDays");
        String taskTemplatesPath = request.getParameter("taskTemplatesPath");
        String taskTagPath = request.getParameter("taskTagPath");
        String taskSeedPath = request.getParameter("taskSeedPath");
        String taskConfigPath = request.getParameter("taskConfigPath");
        String taskClickRegexPath = request.getParameter("taskClickRegexPath");
        String taskProtocolFilterPath = request.getParameter("taskProtocolFilterPath");
        String taskSuffixFilterPath = request.getParameter("taskSuffixFilterPath");
        String taskRegexFilterPath = request.getParameter("taskRegexFilterPath");

        if (taskId.equals("") || taskId == null)
            taskId = RandomUtils.getRandomString(16);

        TaskModel task = new TaskModel();

        task.setTaskId(taskId);
        task.setTaskName(taskName);
        task.setUserId(userId);
        switch (taskType) {
            case "TOPIC":
                task.setTaskType(TaskType.TOPIC);
                break;
            case "WHOLESITE":
                task.setTaskType(TaskType.WHOLESITE);
                break;
            default:
                task.setTaskType(TaskType.UNSET);
                break;
        }
        switch (taskStatus) {
            case "CRAWLING":
                task.setTaskStatus(TaskStatus.CRAWLING);
                break;
            case "CRAWLED":
                task.setTaskStatus(TaskStatus.CRAWLED);
                break;
            case "EXCEPTIOSTOP":
                task.setTaskStatus(TaskStatus.EXCEPTIOSTOP);
                break;
            case "INQUEUE":
                task.setTaskStatus(TaskStatus.INQUEUE);
                break;
            case "UNCRAWL":
                task.setTaskStatus(TaskStatus.UNCRAWL);
                break;
            default:
                task.setTaskStatus(TaskStatus.UNCRAWL);
                break;
        }

        task.setTaskRemark(taskRemark);

        task.setTaskSeedUrl(new TaskSeedUrlModel(taskSeedUrl));
        try {
            task.setTaskCrawlerDepth(Integer.parseInt(taskCrawlerDepth));
        } catch (NumberFormatException e) {
            task.setTaskCrawlerDepth(3);
        }
        try {
            task.setTaskPass(Integer.parseInt(taskPass));
        } catch (NumberFormatException e) {
            task.setTaskPass(3);
        }
        try {
            task.setTaskNodeInstances(Integer.parseInt(taskNodeInstances));
        } catch (NumberFormatException e) {
            task.setTaskNodeInstances(1);
        }
        try {
            task.setTaskInstanceThreads(Integer.parseInt(taskInstanceThreads));
        } catch (NumberFormatException e) {
            task.setTaskInstanceThreads(5);
        }
        try {
            task.setTaskWeight(Integer.parseInt(taskWeight));
        } catch (NumberFormatException e) {
            task.setTaskWeight(10);
        }
        try {
            task.setTaskRecrawlerDays(Integer.parseInt(taskRecrawlerDays));
        } catch (NumberFormatException e) {
            task.setTaskRecrawlerDays(1);
        }

        try {
            task.setTaskStartTime(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(taskStartTime).getTime());
            task.setTaskStopTime(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(taskStopTime).getTime());
        } catch (Exception e) {

        }

        task.setTaskTemplatePath(taskTemplatesPath);
        task.setTaskTagPath(taskTagPath);
        task.setTaskSeedPath(taskSeedPath);
        task.setTaskConfigPath(taskConfigPath);
        task.setTaskClickRegexPath(taskClickRegexPath);
        task.setTaskProtocolFilterPath(taskProtocolFilterPath);
        task.setTaskSuffixFilterPath(taskSuffixFilterPath);
        task.setTaskRegexFilterPath(taskRegexFilterPath);

        TaskSlaveModelDao dao = new TaskSlaveModelDao();

        if (dao.insert(config.getTaskTable(), task) > 0)
            return new ResponseStatus().setStatus(true).toString();

        return new ResponseStatus().setStatus(false).toString();
    }

    @RequestMapping("updateTaskById")
    public String updateTaskById(HttpServletRequest request){

        String taskId = request.getParameter("taskId");
        String userId = request.getParameter("userId");
        String taskName = request.getParameter("taskName");
        String taskType = request.getParameter("taskType");
        String taskStatus = request.getParameter("taskStatus");
        String taskRemark = request.getParameter("taskRemark");
        String taskSeedUrl = request.getParameter("taskSeedUrl");
        String taskCrawlerDepth = request.getParameter("taskCrawlerDepth");
        String taskPass = request.getParameter("taskPass");
        String taskNodeInstances = request.getParameter("taskNodeInstances");
        String taskInstanceThreads = request.getParameter("taskInstanceThreads");
        String taskWeight = request.getParameter("taskWeight");
        String taskStartTime = request.getParameter("taskStartTime");
        String taskStopTime = request.getParameter("taskStopTime");
        String taskRecrawlerDays = request.getParameter("taskRecrawlerDays");
        String taskTemplatesPath = request.getParameter("taskTemplatesPath");
        String taskTagPath = request.getParameter("taskTagPath");
        String taskSeedPath = request.getParameter("taskSeedPath");
        String taskConfigPath = request.getParameter("taskConfigPath");
        String taskClickRegexPath = request.getParameter("taskClickRegexPath");
        String taskProtocolFilterPath = request.getParameter("taskProtocolFilterPath");
        String taskSuffixFilterPath = request.getParameter("taskSuffixFilterPath");
        String taskRegexFilterPath = request.getParameter("taskRegexFilterPath");

        if (taskId.equals("") || taskId == null)
            return new ResponseStatus().setStatus(false).toString();

        TaskModel task = new TaskModel();

        task.setTaskId(taskId);
        task.setTaskName(taskName);
        task.setUserId(userId);
        switch (taskType) {
            case "TOPIC":
                task.setTaskType(TaskType.TOPIC);
                break;
            case "WHOLESITE":
                task.setTaskType(TaskType.WHOLESITE);
                break;
            default:
                task.setTaskType(TaskType.UNSET);
                break;
        }
        switch (taskStatus) {
            case "CRAWLING":
                task.setTaskStatus(TaskStatus.CRAWLING);
                break;
            case "CRAWLED":
                task.setTaskStatus(TaskStatus.CRAWLED);
                break;
            case "EXCEPTIOSTOP":
                task.setTaskStatus(TaskStatus.EXCEPTIOSTOP);
                break;
            case "INQUEUE":
                task.setTaskStatus(TaskStatus.INQUEUE);
                break;
            case "UNCRAWL":
                task.setTaskStatus(TaskStatus.UNCRAWL);
                break;
            default:
                task.setTaskStatus(TaskStatus.UNCRAWL);
                break;
        }

        task.setTaskRemark(taskRemark);

        task.setTaskSeedUrl(new TaskSeedUrlModel(taskSeedUrl));
        try {
            task.setTaskCrawlerDepth(Integer.parseInt(taskCrawlerDepth));
        } catch (NumberFormatException e) {
            task.setTaskCrawlerDepth(3);
        }
        try {
            task.setTaskPass(Integer.parseInt(taskPass));
        } catch (NumberFormatException e) {
            task.setTaskPass(3);
        }
        try {
            task.setTaskNodeInstances(Integer.parseInt(taskNodeInstances));
        } catch (NumberFormatException e) {
            task.setTaskNodeInstances(1);
        }
        try {
            task.setTaskInstanceThreads(Integer.parseInt(taskInstanceThreads));
        } catch (NumberFormatException e) {
            task.setTaskInstanceThreads(5);
        }
        try {
            task.setTaskWeight(Integer.parseInt(taskWeight));
        } catch (NumberFormatException e) {
            task.setTaskWeight(10);
        }
        try {
            task.setTaskRecrawlerDays(Integer.parseInt(taskRecrawlerDays));
        } catch (NumberFormatException e) {
            task.setTaskRecrawlerDays(1);
        }

        try {
            task.setTaskStartTime(new Date(taskStartTime).getTime());
            task.setTaskStopTime(new Date(taskStopTime).getTime());
        } catch (Exception e) {

        }

        task.setTaskTemplatePath(taskTemplatesPath);
        task.setTaskTagPath(taskTagPath);
        task.setTaskSeedPath(taskSeedPath);
        task.setTaskConfigPath(taskConfigPath);
        task.setTaskClickRegexPath(taskClickRegexPath);
        task.setTaskProtocolFilterPath(taskProtocolFilterPath);
        task.setTaskSuffixFilterPath(taskSuffixFilterPath);
        task.setTaskRegexFilterPath(taskRegexFilterPath);

        TaskSlaveModelDao dao = new TaskSlaveModelDao();

        if (dao.update(config.getTaskTable(), task) > 0)
            return new ResponseStatus().setStatus(true).toString();

        return new ResponseStatus().setStatus(false).toString();
    }

    @RequestMapping("getTaskDetailById")
    public String getTaskDetail(HttpServletRequest request) {

        try {

            String taskId = request.getParameter("taskId");
            EntityModel model = new TaskModelDao().selectByTaskId(config.getTaskTable(), taskId);
            if (model == null)
                return new ResponseStatus()
                        .setStatus(false)
                        .setIsRedirect(true)
                        .setLocation("index.html").toString();
            return JSON.toJSONString(model);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return new ResponseStatus()
                .setStatus(false)
                .setIsRedirect(true)
                .setLocation("index.html").toString();
    }

    @RequestMapping("deleteTaskById")
    public String deleteTaskById(HttpServletRequest request) {

        try {
            String taskId = request.getParameter("taskId");
            if (taskId == null)
                new ResponseStatus().setStatus(false).toString();

            TaskModelDao dao = new TaskModelDao();
            TaskModel model = new TaskModel();
            model.setTaskId(taskId);

            if (dao.softDeleteById(config.getTaskTable(), model) > 0)
                return new ResponseStatus().setStatus(true).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseStatus().setStatus(false).toString();
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

            if (dao.deleteById(config.getTaskSlaveTable(), slaveId) > 0)
                return new ResponseStatus().setStatus(true).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseStatus().setStatus(false).toString();
    }


    @RequestMapping("addSlave")
    public String addSlave(HttpServletRequest request) {
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
