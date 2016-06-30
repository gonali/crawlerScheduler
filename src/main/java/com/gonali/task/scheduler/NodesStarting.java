package com.gonali.task.scheduler;


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

    public NodesStarting(TaskModel task, List<NodeInfo> nodeInfoList) {

        this.task = task;
        this.nodeInfoList = new ArrayList<>(nodeInfoList);
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
                }
                else
                    Thread.sleep(aSecond);

            } catch (Exception ex) {

                System.out.println("Exception: at NodesStarting.java, method run() B.");
                ex.printStackTrace();
            }
        }
    }
}
