package com.gonali.task.dao.model;

import com.alibaba.fastjson.JSON;
import com.gonali.task.dao.message.codes.TaskStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskCrawlerInstanceInfoModel implements Serializable {

    private List<InstanceInfo> instanceInfoList;


    private class InstanceInfo {

        private String host = "";
        private int pid;
        private TaskStatus taskStatus;

        public InstanceInfo() {

            host = "0.0.0.0";
            pid = -1;
            taskStatus = TaskStatus.UNCRAWL;
        }

        public InstanceInfo(String host, int pid, TaskStatus taskStatus) {

            this.host = host;
            this.pid = pid;
            this.taskStatus = taskStatus;

        }

        public String getHost() {
            return host;
        }

        public InstanceInfo setHost(String host) {
            this.host = host;
            return this;
        }

        public int getPid() {
            return pid;
        }

        public InstanceInfo setPid(int pid) {
            this.pid = pid;
            return this;
        }

        public TaskStatus getTaskStatus() {
            return taskStatus;
        }

        public InstanceInfo setTaskStatus(TaskStatus taskStatus) {
            this.taskStatus = taskStatus;
            return this;
        }

    }


    public TaskCrawlerInstanceInfoModel() {

        instanceInfoList = new ArrayList<InstanceInfo>();
    }

    public TaskCrawlerInstanceInfoModel addInstance(String host, int pid, TaskStatus taskStatus) {

        this.instanceInfoList.add(new InstanceInfo(host, pid, taskStatus));

        return this;
    }

    public TaskCrawlerInstanceInfoModel removeInstance(String host, int pid) {

        int size = instanceInfoList.size();
        for (int i = 0; i < size; ++i) {

            InstanceInfo instanceInfo = instanceInfoList.get(i);

            if (instanceInfo.getPid() == pid &&
                    instanceInfo.getHost().equals(host)) {
                instanceInfoList.remove(i);
                break;
            }
        }

        return this;
    }


    public TaskCrawlerInstanceInfoModel setInstance(String host, int pid, TaskStatus taskStatus) {

        int size = instanceInfoList.size();
        for (int i = 0; i < size; ++i) {

            InstanceInfo instanceInfo = instanceInfoList.get(i);

            if (instanceInfo.getPid() == pid &&
                    instanceInfo.getHost().equals(host)) {
                instanceInfoList.get(i).setHost(host)
                        .setPid(pid).setTaskStatus(taskStatus);
                break;
            }
        }

        return this;
    }


    public String getCrawlerInstanceInfoJsonString() {

        String jsonString;

        try {

            jsonString = JSON.toJSONString(this.instanceInfoList);

            return jsonString;

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return null;
    }

    public TaskCrawlerInstanceInfoModel setCrawlerInstanceInfo(String jsonString) {

        try {

            this.instanceInfoList = JSON.parseObject(jsonString, List.class);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return this;
    }

}
