package com.gonali.task.dao.scheduler;


import com.gonali.task.dao.config.Config;
import com.gonali.task.dao.message.Message;
import com.gonali.task.dao.message.RuntimeControlMsg;
import com.gonali.task.dao.model.HeartbeatMsgModel;
import com.gonali.task.dao.message.codes.HeartbeatStatusCode;
import com.gonali.task.dao.redisQueue.HeartbeatMsgQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class HeartbeatUpdater implements Runnable {

    private volatile List<HeartbeatMsgModel> heartbeatMsgList;
    private HeartbeatMsgQueue messageQueue;
    private Message message;
    private RuntimeControlMsg runtimeControlMsg;
    private static Config config;
    private static int checkInterval;
    private static int maxTimeoutCount;
    private Lock myLock;


    static {
        config = Config.getConfig();
        try {

            checkInterval = config.getTaskConfig().getSlaveHeartbeatInterval();

        } catch (Exception e) {

            e.printStackTrace();
            checkInterval = 10;
        }

        try {

            maxTimeoutCount = config.getTaskConfig().getMaxHeartbeatTimeoutCount();

        } catch (Exception e) {

            e.printStackTrace();
            maxTimeoutCount = 10;
        }
    }

    public HeartbeatUpdater() {
        runtimeControlMsg = RuntimeControlMsg.getRuntimeControlMsg();
        heartbeatMsgList = new ArrayList<>();
        messageQueue = new HeartbeatMsgQueue();
        myLock = new ReentrantLock();

    }

    @Override
    public void run() {

        while (runtimeControlMsg.isHeartbeatUpdating()) {

            try {

                updateHeartbeatMsg();
                heartbeatTimeoutCheck();

            } catch (Exception e) {
                System.out.println("Exception: at HeartbeatUpdater, method run().");
                e.printStackTrace();
            }

            try {

                Thread.sleep(checkInterval * 10);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }


    private void updateHeartbeatMsg() {

        try {
            myLock.lock();
            int msgSize = heartbeatMsgList.size();
            boolean isHave;

            while ((message = messageQueue.popMessage()) != null) {
                if (msgSize == 0) {
                    heartbeatMsgList.add((HeartbeatMsgModel) message);
                    msgSize = heartbeatMsgList.size();
                    continue;
                }
                isHave = false;
                for (int i = 0; i < msgSize; ++i) {
                    if (heartbeatMsgList.get(i).getTaskId().equals(((HeartbeatMsgModel) message).getTaskId())
                            && heartbeatMsgList.get(i).getHostname().equals(
                            ((HeartbeatMsgModel) message).getHostname()) &&
                            heartbeatMsgList.get(i).getPid() == ((HeartbeatMsgModel) message).getPid()) {

                        heartbeatMsgList.set(i, (HeartbeatMsgModel) message);

                        isHave = true;

                    }
                }

                if (!isHave) {
                    heartbeatMsgList.add((HeartbeatMsgModel) message);
                    msgSize = heartbeatMsgList.size();
                }

            }
        } catch (Exception e) {
            System.out.println("Exception: at HeartbeatUpdater, method updateHeartbeatMsg().");
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
    }

    private void heartbeatTimeoutCheck() {

        try {
            myLock.lock();

            long currentTime = System.currentTimeMillis();
            int size = heartbeatMsgList.size();
            HeartbeatMsgModel h;
            for (int i = 0; i < size; i++) {
                h = heartbeatMsgList.get(i);
                if (currentTime - h.getTime() > 3 * 1000 * HeartbeatUpdater.getCheckInterval() &&
                        h.getStatusCode() != HeartbeatStatusCode.FINISHED) {
                    h.setStatusCode(HeartbeatStatusCode.TIMEOUT);
                    h.setTimeoutCount(h.getTimeoutCount() + 1);
                    heartbeatMsgList.set(i, h);
                } else if (currentTime - h.getTime() > 3 * 1000 * HeartbeatUpdater.getCheckInterval() &&
                        h.getStatusCode() == HeartbeatStatusCode.FINISHED) {
                    //h.setStatusCode(HeartbeatStatusCode.TIMEOUT);
                    h.setTimeoutCount(h.getTimeoutCount() + 1);
                    heartbeatMsgList.set(i, h);
                }

            }
        } catch (Exception e) {
            System.out.println("Exception: at HeartbeatUpdater, method heartbeatTimeoutCheck().");
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
    }

    public void cleanMoreTimeoutHeartbeat(int timesOfMaxTimeout) {

        try {
            myLock.lock();

            List<Integer> index = new ArrayList<>();
            int size = heartbeatMsgList.size();

            for (int i = 0; i < size; i++) {

                if (heartbeatMsgList.get(i).getTimeoutCount() > timesOfMaxTimeout * getMaxTimeoutCount()) {
                    index.add(i);
                }else if (heartbeatMsgList.get(i).getTimeoutCount() >  getMaxTimeoutCount() &&
                        heartbeatMsgList.get(i).getStatusCode() == HeartbeatStatusCode.FINISHED){
                    index.add(i);
                }
            }
            for (Integer i : index)
                heartbeatMsgList.remove(i.intValue());
        } catch (Exception e) {
            System.out.println("Exception: at HeartbeatUpdater, method cleanMoreTimeoutHeartbeat(...).");
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }

    }

    public void resetHeartbeatMsgList() {

        try {
            myLock.lock();

            this.heartbeatMsgList = new ArrayList<>();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
    }

    public void cleanFinishedHeartbeat(String taskId, String hostname, int pid) {

        try {
            myLock.lock();
            List<Integer> index = new ArrayList<>();
            int size = heartbeatMsgList.size();
            for (int i = 0; i < size; i++) {

                if (heartbeatMsgList.get(i).getTaskId().equals(taskId) &&
                        heartbeatMsgList.get(i).getHostname().equals(hostname) &&
                        heartbeatMsgList.get(i).getPid() == pid)
                    index.add(i);
            }

            for (Integer i : index)
                heartbeatMsgList.remove(i.intValue());

        } catch (Exception e) {
            System.out.println("Exception: at HeartbeatUpdater.java, method cleanFinishedHeartbeat(...).");
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }

    }

    public List<HeartbeatMsgModel> getHeartbeatMsgList() {


        try {
            myLock.lock();
            List<HeartbeatMsgModel> newList = new ArrayList<>();

            for (HeartbeatMsgModel msg : this.heartbeatMsgList)
                newList.add(msg);

            return newList;

        } catch (Exception e) {
            System.out.println("Exception: at HeartbeatUpdater.java, method getHeartbeatMsgList().");
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
        return null;
    }

    public HeartbeatMsgModel getHeartbeatMsg(String taskId, String hostname, int pid) {

        try {
            myLock.lock();

            for (HeartbeatMsgModel heartbeat : heartbeatMsgList)
                if (heartbeat.getTaskId().equals(taskId) &&
                        heartbeat.getHostname().equals(hostname) &&
                        heartbeat.getPid() == pid)

                    return heartbeat;

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return null;
    }


    public HeartbeatMsgModel setHeartbeatMsg(String taskId, String hostname, int pid, int statusCode) {

        try {
            myLock.lock();

            int size = heartbeatMsgList.size();
            for (int i = 0; i < size; i++)
                if (heartbeatMsgList.get(i).getTaskId().equals(taskId) &&
                        heartbeatMsgList.get(i).getHostname().equals(hostname) &&
                        heartbeatMsgList.get(i).getPid() == pid) {
                    HeartbeatMsgModel msg = heartbeatMsgList.get(i).setStatusCode(statusCode);
                    return heartbeatMsgList.set(i, msg);
                }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return null;
    }

    public static int getMaxTimeoutCount() {
        return maxTimeoutCount;
    }

    public static int getCheckInterval() {
        return checkInterval;
    }
}
