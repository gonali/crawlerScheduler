package com.gonali.task.model;

/**
 * Created by TianyuanPan on 6/30/16.
 */
public class TaskConfigModel implements EntityModel {

    private int configId;
    private String redisHost;
    private int redisPort;
    private int maxTaskQueueSize;
    private int maxTaskRun;
    private int maxHeartbeatTimeoutCount;
    private int slaveHeartbeatInterval;
    private String slaveAppScript;
    private String adminPassword;


    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public int getMaxTaskQueueSize() {
        return maxTaskQueueSize;
    }

    public void setMaxTaskQueueSize(int maxTaskQueueSize) {
        this.maxTaskQueueSize = maxTaskQueueSize;
    }

    public int getMaxTaskRun() {
        return maxTaskRun;
    }

    public void setMaxTaskRun(int maxTaskRun) {
        this.maxTaskRun = maxTaskRun;
    }

    public int getMaxHeartbeatTimeoutCount() {
        return maxHeartbeatTimeoutCount;
    }

    public void setMaxHeartbeatTimeoutCount(int maxHeartbeatTimeoutCount) {
        this.maxHeartbeatTimeoutCount = maxHeartbeatTimeoutCount;
    }

    public int getSlaveHeartbeatInterval() {
        return slaveHeartbeatInterval;
    }

    public void setSlaveHeartbeatInterval(int slaveHeartbeatInterval) {
        this.slaveHeartbeatInterval = slaveHeartbeatInterval;
    }

    public String getSlaveAppScript() {
        return slaveAppScript;
    }

    public void setSlaveAppScript(String slaveAppScript) {
        this.slaveAppScript = slaveAppScript;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public String getPrimaryKey() {

        return new String("" + configId);
    }

    @Override
    public String insertSqlBuilder(String tableName, EntityModel model) {
        return null;
    }

    @Override
    public String updateSqlBuilder(String tableName, EntityModel model) {

        TaskConfigModel taskConfigModel = (TaskConfigModel) model;
        String sql = "UPDATE " + tableName + " SET redisHost = '" + taskConfigModel.getRedisHost() + "'," +
                "redisPort = '" + taskConfigModel.getRedisPort() + "'," +
                "maxTaskQueueSize = " + taskConfigModel.getMaxTaskQueueSize() + "," +
                "maxTaskRun = " + taskConfigModel.getMaxTaskRun() + "," +
                "maxHeartbeatTimeoutCount = " + taskConfigModel.getMaxHeartbeatTimeoutCount() + "," +
                "slaveHeartbeatInterval = " + taskConfigModel.getSlaveHeartbeatInterval() + "," +
                "slaveAppScript = '" + taskConfigModel.getSlaveAppScript() + "',  " +
                "adminPassword = '" + taskConfigModel.getAdminPassword() + "' " +
                "WHERE configId = " + taskConfigModel.getConfigId();

        return sql;
    }

    @Override
    public String subUpdateSqlBuilder(String tableName, EntityModel model, String... fields) {
        return null;
    }
}
