package com.gonali.task.message;

/**
 * Created by TianyuanPan on 6/30/16.
 */
public class RuntimeControlMsg implements Message {

    private static volatile RuntimeControlMsg msg;

    private boolean isTaskRunning;
    private boolean isHeartbeatUpdating;
    private boolean isCurrentTaskFinished;

    private RuntimeControlMsg() {

        isTaskRunning = false;
        isHeartbeatUpdating = false;
        isCurrentTaskFinished = false;
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


    public boolean isCurrentTaskFinished() {
        return isCurrentTaskFinished;
    }

    public void setIsCurrentTaskFinished(boolean isCurrentTaskFinished) {
        this.isCurrentTaskFinished = isCurrentTaskFinished;
    }

    public void setSchedulerState(boolean isRunning){

        this.setIsHeartbeatUpdating(isRunning);
        this.setIsTaskRunning(isRunning);
        this.setIsCurrentTaskFinished(isRunning);
    }

    @Override
    public Message getMessage() {

        return null;
    }
}
