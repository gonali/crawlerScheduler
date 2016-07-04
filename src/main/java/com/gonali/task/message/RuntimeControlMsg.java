package com.gonali.task.message;

/**
 * Created by TianyuanPan on 6/30/16.
 */
public class RuntimeControlMsg implements Message {

    private static volatile RuntimeControlMsg msg;

    private boolean isTaskRunning;
    private boolean isHeartbeatUpdating;

    private RuntimeControlMsg() {

        isTaskRunning = false;
        isHeartbeatUpdating = true;
    }

    public static RuntimeControlMsg getRuntimeControlMsg() {

        if (msg == null)
            msg = new RuntimeControlMsg();

        return msg;
    }


    public boolean isTaskRunning() {
        return isTaskRunning;
    }

    public void setIsTaskRunning(boolean isTaskRunning) {
        this.isTaskRunning = isTaskRunning;
    }


    public boolean isHeartbeatUpdating() {
        return isHeartbeatUpdating;
    }


    public void setIsHeartbeatUpdating(boolean isHeartbeatUpdating) {
        this.isHeartbeatUpdating = isHeartbeatUpdating;
    }

    @Override
    public Message getMessage() {

        return null;
    }
}
