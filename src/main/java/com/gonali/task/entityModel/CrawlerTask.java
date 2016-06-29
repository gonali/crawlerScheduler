package com.gonali.task.entityModel;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.security.Timestamp;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@Entity
public class CrawlerTask implements Serializable {

    @Id
    @Column(nullable = false)
    private String userId;
    private String taskId;
    private String taskType;
    private String taskRemark;
    private String taskSeedUrl;
    private int taskCrawlerDepth;
    private int taskDynamicDepth;
    private int taskPass;
    private int taskWeight;
    private Timestamp taskStartTime;
    private int taskRecrawlerDays;
    private String taskTemplatePath;
    private String taskTagPath;
    private String taskSeedPath;
    private String taskConfigPath;
    private String taskClickRegexPath;
    private String taskProtocolFilterPath;
    private String taskSuffixFilterPath;
    private String taskRegexFilterPath;
    private String taskStatus;
    private boolean taskDeleteFlag;
    private int taskSeedAmount;
    private int taskSeedImportAmount;
    private int taskCompleteTimes;
    private int taskInstanceThreads;
    private int taskNodeInstances;
    private Timestamp taskStopTime;
    private String taskCrawlerAmountInfo;
    private String taskCrawlerInstanceInfo;

    protected CrawlerTask() {

    }

    public CrawlerTask(String taskId, @Nullable String userId) {

        this.taskId = taskId;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public String getTaskSeedUrl() {
        return taskSeedUrl;
    }

    public void setTaskSeedUrl(String taskSeedUrl) {
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

    public Timestamp getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Timestamp taskStartTime) {
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

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
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

    public void setTaskInstanceThreads(int taskInstanceThreads) {
        this.taskInstanceThreads = taskInstanceThreads;
    }

    public int getTaskNodeInstances() {
        return taskNodeInstances;
    }

    public void setTaskNodeInstances(int taskNodeInstances) {
        this.taskNodeInstances = taskNodeInstances;
    }

    public Timestamp getTaskStopTime() {
        return taskStopTime;
    }

    public void setTaskStopTime(Timestamp taskStopTime) {
        this.taskStopTime = taskStopTime;
    }

    public String getTaskCrawlerAmountInfo() {
        return taskCrawlerAmountInfo;
    }

    public void setTaskCrawlerAmountInfo(String taskCrawlerAmountInfo) {
        this.taskCrawlerAmountInfo = taskCrawlerAmountInfo;
    }

    public String getTaskCrawlerInstanceInfo() {
        return taskCrawlerInstanceInfo;
    }

    public void setTaskCrawlerInstanceInfo(String taskCrawlerInstanceInfo) {
        this.taskCrawlerInstanceInfo = taskCrawlerInstanceInfo;
    }

    @Override
    public String toString() {

        JSONObject json = new JSONObject();

        return "null";
    }
}
