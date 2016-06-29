package com.gonali.task.entityModel;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@Entity
public class CrawlerSlave implements Serializable {

    @Id
    @Column(nullable = false)
    private String slaveId;
    private String slaveUsername;
    private String slavePassword;
    private String slaveIp;
    private String slaveSshPort;
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

    public String getSlaveSshPort() {
        return slaveSshPort;
    }

    public void setSlaveSshPort(String slaveSshPort) {
        this.slaveSshPort = slaveSshPort;
    }

    public String getSlaveAppPath() {
        return slaveAppPath;
    }

    public void setSlaveAppPath(String slaveAppPath) {
        this.slaveAppPath = slaveAppPath;
    }

    @Override
    public String toString() {

        JSONObject json = new JSONObject();

        json.put("slaveId", slaveId);
        json.put("slaveUsername", slaveUsername);
        json.put("slavePassword", slavePassword);
        json.put("slaveIp", slaveIp);
        json.put("slaveSshPort", slaveSshPort);
        json.put("slaveAppPath", slaveAppPath);

        return json.toJSONString();
    }
}
