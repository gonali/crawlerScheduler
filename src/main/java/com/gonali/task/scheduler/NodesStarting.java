package com.gonali.task.scheduler;


import com.gonali.task.message.codes.HeartbeatStatusCode;
import com.gonali.task.model.HeartbeatMsgModel;
import com.gonali.task.model.TaskModel;
import com.gonali.task.nodes.NodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/25/16.
 */
public class NodesStarting implements Runnable {

    private final int aSecond = 1000;

    private TaskModel task;
    private List<NodeInfo> nodeInfoList;
    private HeartbeatUpdater heartbeatUpdater;

    public NodesStarting(TaskModel task, List<NodeInfo> nodeInfoList, HeartbeatUpdater heartbeatUpdater) {

        this.task = task;
        this.nodeInfoList = new ArrayList<>(nodeInfoList);
        this.heartbeatUpdater = heartbeatUpdater;
    }


    @Override
    public void run() {

        boolean isFirst = true;

        for (NodeInfo node : nodeInfoList) {

            String startMsgOut;

            try {

                startMsgOut = node.nodeExecute();
                System.out.println("Starting node [" + node.getHostname() + "] taskId [ " + task.getTaskId() + " ]\noutput: {\n" + startMsgOut + "}");

            } catch (Exception e) {
                System.out.println("Exception: at NodesStarting.java, method run() A.");
                e.printStackTrace();
            }

            try {

                if (isFirst) {
                    Thread.sleep(90 * aSecond);
                    isFirst = false;
                } else
                    Thread.sleep(aSecond);

            } catch (Exception ex) {

                System.out.println("Exception: at NodesStarting.java, method run() B.");
                ex.printStackTrace();
            }

            List<HeartbeatMsgModel> heartbeatMsgModelList = heartbeatUpdater.getHeartbeatMsg(task.getTaskId());

            if (isHeartbeatCodeFinished(heartbeatMsgModelList))
                break;
        }
    }


    private boolean isHeartbeatCodeFinished(List<HeartbeatMsgModel> list) {

        if (list.size() == 0)
            return false;

        for (HeartbeatMsgModel h : list)
            if (h.getStatusCode() == HeartbeatStatusCode.FINISHED)
                return true;

        return false;
    }
}
