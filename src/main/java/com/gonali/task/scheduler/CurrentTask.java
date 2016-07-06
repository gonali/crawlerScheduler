package com.gonali.task.scheduler;



import com.gonali.task.model.HeartbeatMsgModel;
import com.gonali.task.message.codes.HeartbeatStatusCode;
import com.gonali.task.model.TaskModel;
import com.gonali.task.message.codes.TaskStatus;
import com.gonali.task.rulers.base.RulerBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/20/16.
 */
public class CurrentTask {

    private class CurrentTaskModel {

        private TaskModel task;
        private int heartbeatStatusCode;
        private boolean isFinished;
        private List<HeartbeatMsgModel> heartbeatList;

        public CurrentTaskModel(TaskModel task) {

            this.task = task;
            this.task.setTaskStatus(TaskStatus.UNCRAWL);
            this.heartbeatStatusCode = HeartbeatStatusCode.STARTING;
            this.isFinished = false;
            this.heartbeatList = new ArrayList<>();
        }

        public String getTaskId() {
            return task.getTaskId();
        }

        public TaskModel getTask() {
            return task;
        }

        public TaskStatus getTaskStatus() {
            return task.getTaskStatus();
        }

        public void setTaskStatus(TaskStatus status) {
            this.task.setTaskStatus(status);
        }

        public int getHeartbeatStatusCode() {
            return heartbeatStatusCode;
        }

        public void setHeartbeatStatusCode(int heartbeatStatusCode) {
            this.heartbeatStatusCode = heartbeatStatusCode;
        }

        public boolean isFinished() {

            return isFinished;
        }

        public void setIsFinished(boolean isFinished) {
            this.isFinished = isFinished;
        }


        public List<HeartbeatMsgModel> getHeartbeatList() {
            return heartbeatList;
        }

        public void updateHeartbeatList(List<HeartbeatMsgModel> heartbeatList) {


            int msgSize = this.heartbeatList.size();
            boolean isHave;

            if (msgSize == 0) {

                for (HeartbeatMsgModel h : heartbeatList)
                    this.heartbeatList.add(h);

                return;
            }

            for (HeartbeatMsgModel h : heartbeatList) {
                isHave = false;
                for (int i = 0; i < msgSize; ++i) {
                    if (this.heartbeatList.get(i).getHostname().equals(h.getHostname()) &&
                            this.heartbeatList.get(i).getPid() == h.getPid()) {

                        this.heartbeatList.set(i, h);

                        isHave = true;
                        break;
                    }
                }

                if (!isHave)
                    this.heartbeatList.add(h);

                msgSize = this.heartbeatList.size();

            }

            int code = HeartbeatStatusCode.FINISHED;
            for (HeartbeatMsgModel h : this.heartbeatList)
                code += h.getStatusCode();
            this.heartbeatStatusCode = code;
        }


        public void setTaskStopTime(long time){

            this.task.setTaskStopTime(time);
        }

    }


    private CurrentTaskModel[] currentTaskArray;
    private int taskNumber;
    private int nodes;
    private int freeNodes;


    public CurrentTask(int taskNumber) {

        this.taskNumber = taskNumber;
        freeNodes = taskNumber;
        currentTaskArray = new CurrentTaskModel[this.taskNumber];
    }


    public void cleanFinishedHeartbeat(HeartbeatUpdater heartbeatUpdater) {


        for (CurrentTaskModel task : currentTaskArray) {

            if (task == null)
                continue;
            if (task.isFinished) {

                List<HeartbeatMsgModel> heartbeat = task.getHeartbeatList();
                for (HeartbeatMsgModel h : heartbeat)
                    heartbeatUpdater.cleanFinishedHeartbeat(h.getTaskId(), h.getHostname(), h.getPid());
            }
        }

    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public void addTask(TaskModel task) {

        for (int i = 0; i < this.taskNumber; i++) {
            if (this.currentTaskArray[i] == null) {
                this.currentTaskArray[i] = new CurrentTaskModel(task);
                this.currentTaskArray[i].setIsFinished(false);
                freeNodes--;
                break;
            }
            if (this.currentTaskArray[i].isFinished()) {
                this.currentTaskArray[i] = new CurrentTaskModel(task);
                this.currentTaskArray[i].setIsFinished(false);
                freeNodes--;
                break;
            }
        }

    }


    public boolean isHaveFinishedTask() {

        for (CurrentTaskModel task : currentTaskArray) {

            if (task == null)
                return true;

            if (task.isFinished())
                return true;
        }

        return false;
    }

    public void setTaskStatus(String taskId, TaskStatus status) {

        for (int i = 0; i < taskNumber; i++) {

            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].getTaskId().equals(taskId)) {
                currentTaskArray[i].setTaskStatus(status);
            }
        }

    }


    public List<HeartbeatMsgModel> getHeartbeatList(String taskId) {
        for (int i = 0; i < this.taskNumber; i++)
            if (currentTaskArray[i].task.getTaskId().equals(taskId))
                return currentTaskArray[i].getHeartbeatList();
        return null;
    }

    public void setHeartbeatList(List<HeartbeatMsgModel> heartbeatList) {

        for (int i = 0; i < this.taskNumber; i++) {
            String taskId;
            List<HeartbeatMsgModel> list = new ArrayList<>();
            for (HeartbeatMsgModel h : heartbeatList) {
                taskId = h.getTaskId();
                if (currentTaskArray[i] == null)
                    break;
                if (currentTaskArray[i].task.getTaskId().equals(taskId))
                    list.add(h);
            }
            if (currentTaskArray[i] != null) {
                currentTaskArray[i].updateHeartbeatList(list);
                //System.out.println("taskId::" + currentTaskArray[i].getTask().getTaskId() + "::Heartbeat::" + JSON.toJSONString(currentTaskArray[i].getHeartbeatList()));
            }
        }
    }

    public List<TaskModel> getUncrawlTask() {

        List<TaskModel> taskModelList = new ArrayList<>();

        for (int i = 0; i < taskNumber; i++) {
            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].getTaskStatus() == TaskStatus.UNCRAWL)
                taskModelList.add(currentTaskArray[i].getTask());
        }

        return taskModelList;
    }

    public List<TaskModel> getCrawledTask() {

        List<TaskModel> taskModelList = new ArrayList<>();

        for (int i = 0; i < taskNumber; i++) {

            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].isFinished())
                taskModelList.add(currentTaskArray[i].getTask());
        }

        return taskModelList;
    }

    public List<TaskModel> getCurrentTaskElements() {

        List<TaskModel> taskModelList = new ArrayList<>();

        for (int i = 0; i < taskNumber; i++) {

            if (currentTaskArray[i] == null)
                continue;
            taskModelList.add(currentTaskArray[i].getTask());
        }

        return taskModelList;

    }

    public int getFreeNodes() {

        return freeNodes;
    }

    public void taskStatusChecking() {

        for (int i = 0; i < taskNumber; i++) {
            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].isFinished) {
                currentTaskArray[i].setTaskStatus(TaskStatus.CRAWLED);
            }
        }
    }


    public void taskFinishedChecking(TaskScheduler scheduler) {

        int heartbeatCode;

        try {
            for (int i = 0; i < taskNumber; i++) {

                if (currentTaskArray[i] == null)
                    continue;
                int size = currentTaskArray[i].getHeartbeatList().size();

                /*if (size < nodes && currentTaskArray[i].getHeartbeatStatusCode() != HeartbeatStatusCode.TIMEOUT)
                    continue;*/
                if (size == 0)
                    continue;

                heartbeatCode = currentTaskArray[i].getHeartbeatStatusCode();

                if (heartbeatCode == HeartbeatStatusCode.FINISHED) {
                    currentTaskArray[i].setHeartbeatStatusCode(HeartbeatStatusCode.FINISHED);
                    currentTaskArray[i].setTaskStatus(TaskStatus.CRAWLED);
                    currentTaskArray[i].setTaskStopTime(System.currentTimeMillis());
                    currentTaskArray[i].setIsFinished(true);
                    ((RulerBase)scheduler.getRuler()).addToWriteBack(currentTaskArray[i].getTask());
                    freeNodes++;
                    System.out.println("FINISHED TASK: taskId = [ " + currentTaskArray[i].getTaskId() + " ]");
                }

            }
        } catch (Exception e) {
            System.out.println("Exception: at CurrentTask.java, method taskFinishedChecking().");
            e.printStackTrace();
        }
    }

    public void taskNodeTimeoutChecking() {

        List<HeartbeatMsgModel> heartbeatList;

        try {
            for (int i = 0; i < taskNumber; i++) {
                if (currentTaskArray[i] == null)
                    continue;
                if (!currentTaskArray[i].isFinished) {
                    heartbeatList = currentTaskArray[i].getHeartbeatList();
                    HeartbeatMsgModel h;
                    int size = heartbeatList.size();
                    for (int j = 0; j < size; j++) {
                        h = heartbeatList.get(j);
                        if (h.getStatusCode() != HeartbeatStatusCode.FINISHED &&
                                h.getTimeoutCount() > HeartbeatUpdater.getMaxTimeoutCount()) {
                            h.setStatusCode(HeartbeatStatusCode.FINISHED);
                            heartbeatList.set(j, h);
                        }
                    }
                    currentTaskArray[i].updateHeartbeatList(heartbeatList);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: at CurrentTask.java, method taskNodeTimeoutChecking().");
            e.printStackTrace();
        }
    }
}
