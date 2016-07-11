package com.gonali.task.dao.rulers.base;


import com.gonali.task.dao.config.Config;
import com.gonali.task.dao.dao.TaskModelDao;
import com.gonali.task.dao.model.EntityModel;
import com.gonali.task.dao.model.TaskModel;
import com.gonali.task.dao.redisQueue.TaskQueue;
import com.gonali.task.dao.scheduler.CurrentTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TianyuanPan on 6/7/16.
 */
public abstract class RulerBase implements Ruler {

    public static final int IN_QUEUE_ID_FIELD = 0;
    public static final int IN_QUEUE_NAME_FIELD = 1;
    public static final int IN_QUEUE_TIME_FIELD = 2;

    protected static Config config;

    protected CurrentTask currentTasks;

    protected TaskModelDao taskModelDao;
    protected long maxTaskQueueLength;
    protected long currentTaskQueueLength;
    protected String[][] inQueueTaskIdAndNameArray;
    protected String taskTableName = "crawlerTaskTable";
    protected List<EntityModel> writeBackEntityList;

    private Lock myLock;

    static {
        config = Config.getConfig();
    }

    protected RulerBase() {

        try {
            maxTaskQueueLength = config.getTaskConfig().getMaxTaskQueueSize();//Integer.parseInt(ConfigUtils.getResourceBundle().getString("MAX_TASK_QUEUE_LENGTH"));
            taskTableName = config.getTaskTable();//ConfigUtils.getResourceBundle().getString("CRAWLER_TASK_TABLE");
        } catch (NumberFormatException e) {
            maxTaskQueueLength = 100;
            e.printStackTrace();
        }

        //currentTaskQueueLength = TaskQueue.queueLenth();
        inQueueTaskIdAndNameArray = new String[(int) maxTaskQueueLength][3];
        taskModelDao = new TaskModelDao();

        writeBackEntityList = new ArrayList<>();

        myLock = new ReentrantLock();
    }

    public long getMaxTaskQueueLength() {

        return maxTaskQueueLength;
    }

    public long getCurrentTaskQueueLength() {
        currentTaskQueueLength = TaskQueue.queueLenth();
        return currentTaskQueueLength;
    }


    public void addToWriteBack(TaskModel model) {

        writeBackEntityList.add(model);
    }

    public void cleanWriteBackEntityList() {

        writeBackEntityList = new ArrayList<>();
    }

    public boolean isListContainsEntity(TaskModel o) {

        return writeBackEntityList.contains(o);
    }

    public List<String> getInQueueTaskId() {
        List<String> inQueue = new ArrayList<>();
        String new_s;

        try {
            myLock.lock();
            int size = inQueueTaskIdAndNameArray.length;
            for (int i = 0; i < size; i++) {

                if (inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD] == null)
                    continue;
                new_s = new String(inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD]);

                inQueue.add(new_s);
            }

        } catch (Exception e) {
            System.out.println("Exception: at RulerBase.java, method getInQueueTaskIdAndNameArray().");
            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return inQueue;

    }

    public List<String> getInQueueTaskName() {
        List<String> inQueue = new ArrayList<>();
        String new_s;

        try {
            myLock.lock();
            int size = inQueueTaskIdAndNameArray.length;
            for (int i = 0; i < size; i++) {

                if (inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD] == null)
                    continue;
                new_s = new String(inQueueTaskIdAndNameArray[i][IN_QUEUE_NAME_FIELD]);

                inQueue.add(new_s);
            }

        } catch (Exception e) {
            System.out.println("Exception: at RulerBase.java, method getInQueueTaskIdAndNameArray().");
            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return inQueue;

    }

    public String[][] getInQueueTaskIdAndNameArray() {

        String[][] newArray = new String[inQueueTaskIdAndNameArray.length][3];

        try {
            myLock.lock();
            System.arraycopy(inQueueTaskIdAndNameArray, 0, newArray, 0, inQueueTaskIdAndNameArray.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            myLock.unlock();
        }

        return  newArray;
    }

    public void removeInQueueTaskIdAndName(String taskId) {

        try {
            myLock.lock();
            int size = inQueueTaskIdAndNameArray.length;
            int i;
            for (i = 0; i < size; i++) {

                if (inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD] == null)
                    continue;
                if (inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD].equals(taskId)) {
                    inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD] = null;
                    inQueueTaskIdAndNameArray[i][IN_QUEUE_NAME_FIELD] = null;
                    inQueueTaskIdAndNameArray[i][IN_QUEUE_TIME_FIELD] = null;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: at RulerBase.java, method removeInQueueTaskIdAndName(...).");
            e.printStackTrace();
        } finally {
            myLock.unlock();
        }
    }

    public boolean isInQueueTaskIdHaveThis(String taskId) {

        try {
            myLock.lock();

            int size = inQueueTaskIdAndNameArray.length;
            for (int i = 0; i < size; i++) {
                if (inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD] == null)
                    continue;
                if (inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD].equals(taskId))
                    return true;
            }

        } catch (Exception e) {
            System.out.println("Exception: at RulerBase.java, method isInQueueTaskIdHaveThis(...).");
            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return false;
    }

    public void addTaskIdAndNameToInQueue(String taskId, String taskName) {

        if (!isInQueueTaskIdHaveThis(taskId)) {

            myLock.lock();

            try {
                int size = inQueueTaskIdAndNameArray.length;
                for (int i = 0; i < size; i++) {

                    if (inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD] == null) {
                        inQueueTaskIdAndNameArray[i][IN_QUEUE_ID_FIELD] = taskId;
                        inQueueTaskIdAndNameArray[i][IN_QUEUE_NAME_FIELD] = taskName;
                        inQueueTaskIdAndNameArray[i][IN_QUEUE_TIME_FIELD] = Long.toString(System.currentTimeMillis());
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception: at RulerBase.java, method addTaskIdAndNameToInQueue(...).");
                e.printStackTrace();
            } finally {
                myLock.unlock();
            }
        }
    }
}
