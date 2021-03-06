package com.gonali.task.scheduler;


import com.gonali.task.config.Config;
import com.gonali.task.dao.TaskSlaveModelDao;
import com.gonali.task.message.RuntimeControlMsg;
import com.gonali.task.message.codes.HeartbeatStatusCode;
import com.gonali.task.message.codes.TaskStatus;
import com.gonali.task.model.EntityModel;
import com.gonali.task.model.HeartbeatMsgModel;
import com.gonali.task.model.TaskModel;
import com.gonali.task.model.TaskSlaveModel;
import com.gonali.task.nodes.NodeInfo;
import com.gonali.task.redisQueue.HeartbeatMsgQueue;
import com.gonali.task.redisQueue.TaskQueue;
import com.gonali.task.rulers.SimpleLongTimeFirstRuler;
import com.gonali.task.rulers.base.RulerBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class TaskScheduler {

    private static volatile TaskScheduler scheduler;

    private RuntimeControlMsg runtimeControlMsg;
    private CurrentTask currentTasks;
    private HeartbeatUpdater heartbeatUpdater;
    private List<NodeInfo> nodeInfoList;
    private String command = "echo \" Hello World !! \"";
    private String sh;
    private RulerBase ruler;
    private Config config;
    private int nodeNumber;
    private List<TaskSlaveModel> slaveList;


    public static CurrentTask getSchedulerCurrentTask() {

        if (scheduler == null)
            return null;
        return scheduler.getCurrentTasks();
    }

    public static List<HeartbeatMsgModel> getSchedulerHeartbeatMsgList() {

        if (scheduler == null)
            return null;
        return scheduler.heartbeatUpdater.getHeartbeatMsgList();
    }

    public static List<NodeInfo> getSchedulerNodeInfoList() {

        if (scheduler == null)
            return null;
        return scheduler.getNodeInfoList();
    }


    private TaskScheduler() {

        config = Config.getConfig();
        runtimeControlMsg = RuntimeControlMsg.getRuntimeControlMsg();
        heartbeatUpdater = new HeartbeatUpdater();
        try {

            this.sh = config.getTaskConfig().getSlaveAppScript();

        } catch (Exception e) {
            sh = "crawlerStart.sh";
            e.printStackTrace();
        }
        int currentTaskSize;

        try {
            currentTaskSize = config.getTaskConfig().getMaxTaskRun();
        } catch (Exception e) {
            currentTaskSize = 3;
            e.printStackTrace();
        }

        try {
            slaveList = new ArrayList<>();
            List<EntityModel> modelList = new TaskSlaveModelDao().selectAll(config.getTaskSlaveTable());
            for (EntityModel m : modelList)
                slaveList.add((TaskSlaveModel) m);

        } catch (Exception e) {

            e.printStackTrace();
        }


        try {
            nodeNumber = slaveList.size();
        } catch (Exception e) {
            nodeNumber = 0;
            e.printStackTrace();
        }

        currentTasks = new CurrentTask(currentTaskSize);
        currentTasks.setNodes(nodeNumber);

    }


    public TaskScheduler registerRuler(RulerBase ruler) {

        if (ruler == null) {
            this.ruler = new SimpleLongTimeFirstRuler();
            return this;
        }
        this.ruler = ruler;
        return this;

    }

    /**
     * $(1):  depth
     * $(2):  pass
     * $(3):  tid
     * $(4):  startTime
     * $(5):  seedPath
     * $(6):  protocolDir
     * $(7):  type
     * $(8):  recallDepth
     * $(9):  templateDir
     * $(10): clickRegexDir
     * $(11): postRegexDir
     * $(12): configPath
     */
    private void setTaskStartInfo(TaskModel currentTask) {

        command = sh +
                "  " + currentTask.getTaskCrawlerDepth() +
                "  " + currentTask.getTaskPass() +
                "  " + currentTask.getTaskId() +
                "  " + currentTask.getTaskStartTime() +
                "  " + currentTask.getTaskSeedPath() +
                "  " + currentTask.getTaskProtocolFilterPath() +
                "  " + currentTask.getTaskType() +
                "  " + currentTask.getTaskCrawlerDepth() +
                "  " + currentTask.getTaskTemplatePath() +
                "  " + currentTask.getTaskClickRegexPath() +
                "  " + currentTask.getTaskRegexFilterPath() +
                "  " + currentTask.getTaskConfigPath();


        NodeInfo nodeInfo;

        nodeInfoList = new ArrayList<>();

        for (int i = 0; i < nodeNumber; ++i) {

            nodeInfo = new NodeInfo(slaveList.get(i).getSlaveUsername(),
                    slaveList.get(i).getSlavePassword(),
                    slaveList.get(i).getSlaveIp(), command);

            nodeInfoList.add(nodeInfo);
        }

    }


    private void killTimeoutNodes(CurrentTask currentTasks) {

        NodeInfo nodeInfo;
        List<HeartbeatMsgModel> heartbeatMsgModelList;


        List<TaskModel> taskModelList = currentTasks.getCurrentTaskElements();
        nodeInfoList = new ArrayList<>();

        try {
            for (TaskModel t : taskModelList) {

                heartbeatMsgModelList = currentTasks.getHeartbeatList(t.getTaskId());

                for (HeartbeatMsgModel h : heartbeatMsgModelList) {

                    if (h.getStatusCode() != HeartbeatStatusCode.FINISHED &&
                            h.getTimeoutCount() > HeartbeatUpdater.getMaxTimeoutCount()) {

                        for (TaskSlaveModel m : slaveList) {
                            if (m.getSlaveIp().equals(h.getHostname())) {

                                nodeInfo = new NodeInfo(m.getSlaveUsername(),
                                        m.getSlavePassword(),
                                        m.getSlaveIp(), "kill -9 " + h.getPid());
                                nodeInfo.setPort(m.getSlaveSshPort());
                                nodeInfoList.add(nodeInfo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: at TaskScheduler.java, method killTimeoutNodes(...) A.");
            e.printStackTrace();
        }

        try {
            for (NodeInfo n : nodeInfoList) {

                String output = n.nodeExecute();

                System.out.println("===>> kill timeout node, COMMAND = [ " + n.getCommand() + " ]:" + output);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: at TaskScheduler.java, method killTimeoutNodes(...) B.");
            e.printStackTrace();
        }
    }


    private void cleanTaskQueue() {

        while (TaskQueue.popCrawlerTaskQueue() != null) ;
    }


    private void cleanHeartbeatMsgQueue() {

        HeartbeatMsgQueue queue = new HeartbeatMsgQueue();

        while (queue.popMessage() != null) ;
    }


    public static TaskScheduler createTaskScheduler() {

        if (scheduler == null)
            scheduler = new TaskScheduler();
        return scheduler;
    }


    public void schedulerStart() {


        while (true) {

            try {

                if (runtimeControlMsg.isTaskRunning()) {

                    cleanTaskQueue();
                    cleanHeartbeatMsgQueue();
                    //new Thread(heartbeatUpdater).start();
                    runtimeControlMsg.setIsCurrentTaskFinished(false);
                }

                while (runtimeControlMsg.isTaskRunning() || !runtimeControlMsg.isCurrentTasksFinished()) {

                    try {
                        ruler.writeBack(this);
                        currentTasks = ruler.doSchedule(this);

                        List<TaskModel> tasks = currentTasks.getUncrawlTask();

                        for (TaskModel t : tasks) {

                            t.setTaskStatus(TaskStatus.CRAWLING);
                            currentTasks.setTaskStatus(t.getTaskId(), t.getTaskStatus());
                            ruler.addToWriteBack(t);

                            setTaskStartInfo(t);

                            System.out.println("Starting task, Id = [ " + t.getTaskId() + " ]");
                            new Thread(new NodesStarting(t, nodeInfoList, heartbeatUpdater)).start();

                        }

                        heartbeatUpdater.doUpdate();

                        List<HeartbeatMsgModel> heartbeatList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();
                        currentTasks.setHeartbeatList(heartbeatList);

                        killTimeoutNodes(currentTasks);
                        currentTasks.taskStatusChecking();
                        currentTasks.taskNodeTimeoutChecking();
                        currentTasks.taskFinishedChecking(this);
                        currentTasks.cleanFinishedHeartbeat(this.heartbeatUpdater);

                        heartbeatUpdater.cleanMoreTimeoutHeartbeat(config.getTaskConfig().getMaxHeartbeatTimeoutCount());

                        if (!currentTasks.isTasksFinished()) {

                            runtimeControlMsg.setIsCurrentTaskFinished(false);
                            runtimeControlMsg.setIsHeartbeatUpdating(true);

                        } else {

                            runtimeControlMsg.setIsCurrentTaskFinished(true);
                            runtimeControlMsg.setIsHeartbeatUpdating(false);
                        }

                    } catch (Exception e) {
                        System.out.println("Exception: at TaskScheduler.java, method schedulerStart() B.");
                        e.printStackTrace();
                    }

                    try {

                        Thread.sleep(500);

                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }

                }

                runtimeControlMsg.setIsTaskRunning(false);


                try {

                    Thread.sleep(500);

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }


            } catch (Exception ex) {

                runtimeControlMsg.setIsCurrentTaskFinished(true);
                runtimeControlMsg.setIsHeartbeatUpdating(false);
                runtimeControlMsg.setIsTaskRunning(false);
                ex.printStackTrace();
            }


            try {

                Thread.sleep(500);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }

    }


    public RuntimeControlMsg getRuntimeControlMsg() {
        return runtimeControlMsg;
    }

    public CurrentTask getCurrentTasks() {
        return currentTasks;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public List<NodeInfo> getNodeInfoList() {
        return nodeInfoList;
    }

    public String getCommand() {
        return command;
    }

    public RulerBase getRuler() {
        return ruler;
    }


    public HeartbeatUpdater getHeartbeatUpdater() {
        return heartbeatUpdater;
    }

}
