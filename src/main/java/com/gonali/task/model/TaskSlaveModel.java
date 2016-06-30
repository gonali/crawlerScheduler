package com.gonali.task.model;

/**
 * Created by TianyuanPan on 6/30/16.
 */
public class TaskSlaveModel implements EntityModel{

    private String slaveId;
    private String slaveUsername;
    private String slavePassword;
    private String slaveIp;
    private int slaveSshPort = 22;
    private String slaveAppPath;


    public String getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(String slaveId) {
        this.slaveId = slaveId;
    }

    public String getSlaveUsername() {
        return slaveUsername;
    }

    public void setSlaveUsername(String slaveUsername) {
        this.slaveUsername = slaveUsername;
    }

    public String getSlavePassword() {
        return slavePassword;
    }

    public void setSlavePassword(String slavePassword) {
        this.slavePassword = slavePassword;
    }

    public String getSlaveIp() {
        return slaveIp;
    }

    public void setSlaveIp(String slaveIp) {
        this.slaveIp = slaveIp;
    }

    public int getSlaveSshPort() {
        return slaveSshPort;
    }

    public void setSlaveSshPort(int slaveSshPort) {
        this.slaveSshPort = slaveSshPort;
    }

    public String getSlaveAppPath() {
        return slaveAppPath;
    }

    public void setSlaveAppPath(String slaveAppPath) {
        this.slaveAppPath = slaveAppPath;
    }

    @Override
    public String getPrimaryKey() {
        return null;
    }

    @Override
    public String insertSqlBuilder(String tableName, EntityModel model) {
        return null;
    }

    @Override
    public String updateSqlBuilder(String tableName, EntityModel model) {
        return null;
    }

    @Override
    public String subUpdateSqlBuilder(String tableName, EntityModel model, String... fields) {
        return null;
    }
}
