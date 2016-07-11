package com.gonali.task.dao.nodes;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by TianyuanPan on 6/4/16.
 */

public class NodeInfo {

    private String username;
    private String password;
    private String hostname;
    private int port = 22;

    private String command;

    /* shell utils */
    private ShellUtils shellUtils;


    public NodeInfo(String username, String password, String hostname, String command) {

        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.command = command;
    }

    public String nodeExecute() {
        if (shellUtils == null)
            shellUtils = ShellUtils.getShellUtils(this.username, this.password, this.hostname, this.port);

        return this.shellUtils.doExecuteShell(command);
    }


    public String getUsername() {
        return username;
    }

    public NodeInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public NodeInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public NodeInfo setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public int getPort() {
        return port;
    }

    public NodeInfo setPort(int port) {
        this.port = port;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public NodeInfo setCommand(String command) {
        this.command = command;
        return this;
    }

    public ShellUtils getShell() {
        return shellUtils;
    }

    public NodeInfo setShell(ShellUtils shell) {
        this.shellUtils = shell;
        return this;
    }

    @Override
    public String toString(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("host", hostname);
        jsonObject.put("port", port);

        return jsonObject.toJSONString();
    }
}
