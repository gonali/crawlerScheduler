package com.gonali.task.dao.model;

import com.gonali.task.dao.message.codes.TaskStatus;
import com.gonali.task.dao.message.codes.TaskType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskModel implements EntityModel {

    private String taskId = "";
    private TaskType taskType;
    private String taskName = "";
    private String taskRemark = "";
    private TaskSeedUrlModel taskSeedUrl;
    private int taskCrawlerDepth;
    private int taskDynamicDepth;
    private int taskPass;
    private int taskWeight;
    private long taskStartTime;
    private int taskRecrawlerDays;
    private String taskTemplatePath = "";
    private String taskTagPath = "";
    private String taskSeedPath = "";
    private String taskConfigPath = "";
    private String taskClickRegexPath = "";
    private String taskProtocolFilterPath = "";
    private String taskSuffixFilterPath = "";
    private String taskRegexFilterPath = "";
    private TaskStatus taskStatus;
    private boolean taskDeleteFlag;
    private int taskSeedAmount;
    private int taskSeedImportAmount;
    private int taskCompleteTimes;
    private int taskInstanceThreads;
    private int taskNodeInstances;
    private long taskStopTime;
    private TaskCrawlerAmountInfoModel taskCrawlerAmountInfo;
    private TaskCrawlerInstanceInfoModel taskCrawlerInstanceInfo;

    private TaskUserModel taskUser;


    public TaskModel() {

        this.taskDeleteFlag = false;
        this.taskType = TaskType.UNSET;
        this.taskStatus = TaskStatus.UNCRAWL;
        this.taskInstanceThreads = 1;
        this.taskCrawlerAmountInfo = new TaskCrawlerAmountInfoModel();
        this.taskCrawlerInstanceInfo = new TaskCrawlerInstanceInfoModel();
        this.taskUser = new TaskUserModel();
    }

    public TaskModel(String taskId, String userId) {

        this.taskCrawlerAmountInfo = new TaskCrawlerAmountInfoModel();
        this.taskCrawlerInstanceInfo = new TaskCrawlerInstanceInfoModel();
        this.taskUser = new TaskUserModel();

        this.taskId = taskId;
        this.taskUser.setUserId(userId);
        this.taskDeleteFlag = false;
        this.taskType = TaskType.UNSET;
        this.taskStatus = TaskStatus.UNCRAWL;
        this.taskInstanceThreads = 1;
    }


    public String getUserId() {
        return this.taskUser.getUserId();
    }

    public void setUserId(String userId) {
        this.taskUser.setUserId(userId);
    }

    public String getUserAppkey() {
        return this.taskUser.getUserAppkey();
    }

    public void setUserAppkey(String userAppkey) {
        this.taskUser.setUserAppkey(userAppkey);
    }

    public String getUserDescription() {
        return this.taskUser.getUserDescription();
    }

    public void setUserDescription(String userDescription) {
        this.taskUser.setUserDescription(userDescription);
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public TaskSeedUrlModel getTaskSeedUrl() {
        return taskSeedUrl;
    }

    public void setTaskSeedUrl(TaskSeedUrlModel taskSeedUrl) {
        this.taskSeedUrl = taskSeedUrl;
    }

    public int getTaskCrawlerDepth() {
        return taskCrawlerDepth;
    }

    public void setTaskCrawlerDepth(int taskCrawlerDepth) {
        this.taskCrawlerDepth = taskCrawlerDepth;
    }

    public int getTaskDynamicDepth() {
        return taskDynamicDepth;
    }

    public void setTaskDynamicDepth(int taskDynamicDepth) {
        this.taskDynamicDepth = taskDynamicDepth;
    }

    public int getTaskPass() {
        return taskPass;
    }

    public void setTaskPass(int taskPass) {
        this.taskPass = taskPass;
    }

    public int getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(int taskWeight) {
        this.taskWeight = taskWeight;
    }

    public long getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public int getTaskRecrawlerDays() {
        return taskRecrawlerDays;
    }

    public void setTaskRecrawlerDays(int taskRecrawlerDays) {
        this.taskRecrawlerDays = taskRecrawlerDays;
    }

    public String getTaskTemplatePath() {
        return taskTemplatePath;
    }

    public void setTaskTemplatePath(String taskTemplatePath) {
        this.taskTemplatePath = taskTemplatePath;
    }

    public String getTaskTagPath() {
        return taskTagPath;
    }

    public void setTaskTagPath(String taskTagPath) {
        this.taskTagPath = taskTagPath;
    }

    public String getTaskSeedPath() {
        return taskSeedPath;
    }

    public void setTaskSeedPath(String taskSeedPath) {
        this.taskSeedPath = taskSeedPath;
    }

    public String getTaskConfigPath() {
        return taskConfigPath;
    }

    public void setTaskConfigPath(String taskConfigPath) {
        this.taskConfigPath = taskConfigPath;
    }

    public String getTaskClickRegexPath() {
        return taskClickRegexPath;
    }

    public void setTaskClickRegexPath(String taskClickRegexPath) {
        this.taskClickRegexPath = taskClickRegexPath;
    }

    public String getTaskProtocolFilterPath() {
        return taskProtocolFilterPath;
    }

    public void setTaskProtocolFilterPath(String taskProtocolFilterPath) {
        this.taskProtocolFilterPath = taskProtocolFilterPath;
    }

    public String getTaskSuffixFilterPath() {
        return taskSuffixFilterPath;
    }

    public void setTaskSuffixFilterPath(String taskSuffixFilterPath) {
        this.taskSuffixFilterPath = taskSuffixFilterPath;
    }

    public String getTaskRegexFilterPath() {
        return taskRegexFilterPath;
    }

    public void setTaskRegexFilterPath(String taskRegexFilterPath) {
        this.taskRegexFilterPath = taskRegexFilterPath;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean isTaskDeleteFlag() {
        return taskDeleteFlag;
    }

    public void setTaskDeleteFlag(boolean taskDeleteFlag) {
        this.taskDeleteFlag = taskDeleteFlag;
    }

    public int getTaskSeedAmount() {
        return taskSeedAmount;
    }

    public void setTaskSeedAmount(int taskSeedAmount) {
        this.taskSeedAmount = taskSeedAmount;
    }

    public int getTaskSeedImportAmount() {
        return taskSeedImportAmount;
    }

    public void setTaskSeedImportAmount(int taskSeedImportAmount) {
        this.taskSeedImportAmount = taskSeedImportAmount;
    }

    public int getTaskCompleteTimes() {
        return taskCompleteTimes;
    }

    public void setTaskCompleteTimes(int taskCompleteTimes) {
        this.taskCompleteTimes = taskCompleteTimes;
    }

    public int getTaskInstanceThreads() {
        return taskInstanceThreads;
    }

    public int getTaskNodeInstances() {
        return taskNodeInstances;
    }

    public void setTaskNodeInstances(int taskNodeInstances) {
        this.taskNodeInstances = taskNodeInstances;
    }

    public void setTaskInstanceThreads(int taskInstanceThreads) {
        this.taskInstanceThreads = taskInstanceThreads;
    }

    public long getTaskStopTime() {
        return taskStopTime;
    }

    public void setTaskStopTime(long taskStopTime) {
        this.taskStopTime = taskStopTime;
    }

    public TaskCrawlerAmountInfoModel getTaskCrawlerAmountInfo() {
        return taskCrawlerAmountInfo;
    }

    public void setTaskCrawlerAmountInfo(TaskCrawlerAmountInfoModel taskCrawlerAmountInfo) {
        this.taskCrawlerAmountInfo = taskCrawlerAmountInfo;
    }

    public TaskCrawlerInstanceInfoModel getTaskCrawlerInstanceInfo() {
        return taskCrawlerInstanceInfo;
    }

    public void setTaskCrawlerInstanceInfo(TaskCrawlerInstanceInfoModel taskCrawlerInstanceInfo) {
        this.taskCrawlerInstanceInfo = taskCrawlerInstanceInfo;
    }

    public TaskUserModel getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(TaskUserModel taskUser) {
        this.taskUser = taskUser;
    }


    @Override
    public String getPrimaryKey() {
        return this.taskId;
    }

    @Override
    public String insertSqlBuilder(String tableName, EntityModel taskModel) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String values = "'" + ((TaskModel) taskModel).getUserId() + "'," +
                "'" + ((TaskModel) taskModel).getTaskId() + "'," +
                "'" + ((TaskModel) taskModel).getTaskType() + "'," +
                "'" + ((TaskModel) taskModel).getTaskName() + "'," +
                "'" + ((TaskModel) taskModel).getTaskRemark() + "'," +
                "'" + ((TaskModel) taskModel).getTaskSeedUrl().getSeedurlJsonString() + "'," +
                ((TaskModel) taskModel).getTaskCrawlerDepth() + "," +
                ((TaskModel) taskModel).getTaskDynamicDepth() + "," +
                ((TaskModel) taskModel).getTaskPass() + "," +
                ((TaskModel) taskModel).getTaskWeight() + "," +
                "'" + timeFormat.format(new Date(((TaskModel) taskModel).getTaskStartTime()))  + "'," +
                ((TaskModel) taskModel).getTaskRecrawlerDays() + "," +
                "'" + ((TaskModel) taskModel).getTaskTemplatePath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskTagPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskSeedPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskConfigPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskClickRegexPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskProtocolFilterPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskSuffixFilterPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskRegexFilterPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskStatus() + "'," +
                ((TaskModel) taskModel).isTaskDeleteFlag() + "," +
                ((TaskModel) taskModel).getTaskSeedAmount() + "," +
                ((TaskModel) taskModel).getTaskSeedImportAmount() + "," +
                ((TaskModel) taskModel).getTaskCompleteTimes()  + "," +
                ((TaskModel) taskModel).getTaskInstanceThreads() + "," +
                ((TaskModel) taskModel).getTaskNodeInstances() + "," +
                "'" + timeFormat.format(new Date(((TaskModel) taskModel).getTaskStopTime()))  + "'," +
                "'" + ((TaskModel) taskModel).getTaskCrawlerAmountInfo().getCrawlerAmountInfoJsonString() + "'," +
                "'" + ((TaskModel) taskModel).getTaskCrawlerInstanceInfo().getCrawlerInstanceInfoJsonString() + "'";


        String sql = "INSERT INTO " + tableName +
                "(userId, taskId, " +
                " taskType, taskName, taskRemark, taskSeedUrl, taskCrawlerDepth, " +
                " taskDynamicDepth, taskPass, taskWeight, taskStartTime, " +
                " taskRecrawlerDays, taskTemplatePath, taskTagPath, taskSeedPath," +
                " taskConfigPath, taskClickRegexPath, " +
                " taskProtocolFilterPath, taskSuffixFilterPath, taskRegexFilterPath, " +
                " taskStatus, taskDeleteFlag, taskSeedAmount, taskSeedImportAmount, " +
                " taskCompleteTimes, taskInstanceThreads, taskNodeInstances, taskStopTime, " +
                " taskCrawlerAmountInfo, taskCrawlerInstanceInfo) VALUES (" + values + ")";

        return sql;
    }

    @Override
    public String updateSqlBuilder(String tableName, EntityModel taskModel) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String sql = "UPDATE  " + tableName + "  " +
                "SET userId = " + "'" + ((TaskModel) taskModel).getUserId() + "'," +
                " taskType = " + "'" + ((TaskModel) taskModel).getTaskType() + "'," +
                " taskName = " + "'" + ((TaskModel) taskModel).getTaskName() + "'," +
                " taskRemark = " + "'" + ((TaskModel) taskModel).getTaskRemark() + "'," +
                " taskSeedUrl = " + "'" + ((TaskModel) taskModel).getTaskSeedUrl().getSeedurlJsonString() + "'," +
                " taskCrawlerDepth = " + ((TaskModel) taskModel).getTaskCrawlerDepth() + "," +
                " taskDynamicDepth = " + ((TaskModel) taskModel).getTaskDynamicDepth() + "," +
                " taskPass = " + ((TaskModel) taskModel).getTaskPass() + "," +
                " taskWeight = " + ((TaskModel) taskModel).getTaskWeight() + "," +
                " taskStartTime = '" + timeFormat.format(new Date(((TaskModel) taskModel).getTaskStartTime())) + "'," +
                " taskRecrawlerDays = " + ((TaskModel) taskModel).getTaskRecrawlerDays() + "," +
                " taskTemplatePath = " + "'" + ((TaskModel) taskModel).getTaskTemplatePath() + "'," +
                " taskTagPath = " + "'" + ((TaskModel) taskModel).getTaskTagPath() + "'," +
                " taskSeedPath = " + "'" + ((TaskModel) taskModel).getTaskSeedPath() + "'," +
                " taskConfigPath = " + "'" + ((TaskModel) taskModel).getTaskConfigPath() + "'," +
                " taskClickRegexPath = " + "'" + ((TaskModel) taskModel).getTaskClickRegexPath() + "'," +
                " taskProtocolFilterPath = " + "'" + ((TaskModel) taskModel).getTaskProtocolFilterPath() + "'," +
                " taskSuffixFilterPath = " + "'" + ((TaskModel) taskModel).getTaskSuffixFilterPath() + "'," +
                " taskRegexFilterPath = " + "'" + ((TaskModel) taskModel).getTaskRegexFilterPath() + "'," +
                " taskStatus = " + "'" + ((TaskModel) taskModel).getTaskStatus() + "'," +
                " taskDeleteFlag = " + ((TaskModel) taskModel).isTaskDeleteFlag() + "," +
                " taskSeedAmount = " + ((TaskModel) taskModel).getTaskSeedAmount() + "," +
                " taskSeedImportAmount = " + ((TaskModel) taskModel).getTaskSeedImportAmount() + "," +
                " taskCompleteTimes = " + ((TaskModel) taskModel).getTaskCompleteTimes() + "," +
                " taskInstanceThreads = " + ((TaskModel) taskModel).getTaskInstanceThreads() + "," +
                " taskNodeInstances = " + ((TaskModel) taskModel).getTaskNodeInstances() + "," +
                " taskStopTime = '" + timeFormat.format(new Date(((TaskModel) taskModel).getTaskStopTime())) + "'," +
                " taskCrawlerAmountInfo = " + "'" + ((TaskModel) taskModel).getTaskCrawlerAmountInfo().getCrawlerAmountInfoJsonString() + "'," +
                " taskCrawlerInstanceInfo =  " + "'" + ((TaskModel) taskModel).getTaskCrawlerInstanceInfo().getCrawlerInstanceInfoJsonString() + "'   " +
                "WHERE taskId = " + "'" + ((TaskModel) taskModel).getTaskId() + "'";

        return sql;
    }

    @Override
    public String subUpdateSqlBuilder(String tableName, EntityModel model, String... fields) {
        return null;
    }


}
