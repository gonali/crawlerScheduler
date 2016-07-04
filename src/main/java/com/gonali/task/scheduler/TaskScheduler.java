package com.gonali.task.scheduler;


import com.gonali.task.message.RuntimeControlMsg;
import com.gonali.task.message.codes.HeartbeatStatusCode;
import com.gonali.task.message.codes.TaskStatus;
import com.gonali.task.model.HeartbeatMsgModel;
import com.gonali.task.model.TaskModel;
import com.gonali.task.nodes.NodeInfo;
import com.gonali.task.redisQueue.HeartbeatMsgQueue;
import com.gonali.task.redisQueue.TaskQueue;
import com.gonali.task.rulers.SimpleLongTimeFirstRuler;
import com.gonali.task.rulers.base.Ruler;
import com.gonali.task.rulers.base.RulerBase;
import com.gonali.task.utils.ConfigUtils;

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

    private int nodeNumber;
    private List<String[]> hostInfoList;


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

        runtimeControlMsg = RuntimeControlMsg.getRuntimeControlMsg();
        heartbeatUpdater = new HeartbeatUpdater();
        try {

            this.sh = ConfigUtils.getResourceBundle().getString("NODE_START_SH");

        } catch (Exception e) {
            sh = "crawlerStart.sh";
            e.printStackTrace();
        }
        int currentTaskSize;

        try {
            currentTaskSize = Integer.parseInt(ConfigUtils.getResourceBundle().getString("CURRENT_TASK_ARRAY_SIZE"));
        } catch (NumberFormatException e) {
            currentTaskSize = 3;
            e.printStackTrace();
        }

        try {
            nodeNumber = Integer.parseInt(ConfigUtils.getResourceBundle("nodes").getString("NODES"));
        } catch (NumberFormatException e) {
            nodeNumber = 0;
            e.printStackTrace();
        }


        hostInfoList = new ArrayList<>();

        try {
            for (int i = 1; i <= nodeNumber; ++i) {
                String info = ConfigUtils.getResourceBundle("nodes").getString("NODE_" + i);
                hostInfoList.add(info.split("::"));
            }
        } catch (Exception e) {

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

            nodeInfo = new NodeInfo(hostInfoList.get(i)[0], hostInfoList.get(i)[1], hostInfoList.get(i)[2], command);
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

                        for (String[] ss : hostInfoList) {
                            if (ss[2].equals(h.getHostname())) {
                                nodeInfo = new NodeInfo(ss[0], ss[1], ss[2], "kill -9 " + h.getPid());
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

            if (runtimeControlMsg.isTaskRunning()) {

                cleanTaskQueue();
                cleanHeartbeatMsgQueue();
                new Thread(heartbeatUpdater).start();

                while (true) {

                    try {
                        ruler.writeBack(this);
                        currentTasks = ruler.doSchedule(this);

                        List<TaskModel> tasks = currentTasks.getUncrawlTask();

                        for (TaskModel t : tasks) {

                            t.setTaskStatus(TaskStatus.CRAWLING);
                            currentTasks.setTaskStatus(t.getTaskId(), t.getTaskStatus());
                            ((RulerBase) ruler).addToWriteBack(t);

                            setTaskStartInfo(t);

                            System.out.println("Starting task, Id = [ " + t.getTaskId() + " ]");
                            new Thread(new NodesStarting(t, nodeInfoList)).start();

                   /* for (NodeInfo node : nodeInfoList) {

                        try {

                            String startMsgOut = node.nodeExecute();
                            System.out.println("Starting node [" + node.getHostname() + "]\noutput: {\n" + startMsgOut + "}");
                            Thread.sleep(100);

                        } catch (Exception ex) {
                            System.out.println("Exception: at TaskScheduler.java, method schedulerStart() A.");
                            ex.printStackTrace();
                        }
                    }*/
                        }

                        killTimeoutNodes(currentTasks);

                        currentTasks.taskStatusChecking();
                        currentTasks.taskNodeTimeoutChecking();
                        currentTasks.taskFinishedChecking(this);
                        currentTasks.cleanFinishedHeartbeat(this.heartbeatUpdater);

                        heartbeatUpdater.cleanMoreTimeoutHeartbeat(30);

                        //System.out.println("Is CurrentTaskArray have finished task ? : " + currentTasks.isHaveFinishedTask());

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
            }


            try {

                Thread.sleep(500);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }

    }

    public List<String[]> getHostInfoList() {

        return hostInfoList;
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

    public Ruler getRuler() {
        return ruler;
    }


    public HeartbeatUpdater getHeartbeatUpdater() {
        return heartbeatUpdater;
    }

}
