package com.gonali.task.rulers.base;



import com.gonali.task.dao.TaskModelDao;
import com.gonali.task.model.EntityModel;
import com.gonali.task.model.TaskModel;
import com.gonali.task.redisQueue.TaskQueue;
import com.gonali.task.scheduler.CurrentTask;
import com.gonali.task.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TianyuanPan on 6/7/16.
 */
public abstract class RulerBase implements Ruler {

    protected CurrentTask currentTasks;

    protected TaskModelDao taskModelDao;
    protected long maxTaskQueueLength;
    protected long currentTaskQueueLength;
    protected String[] inQueueTaskIdArray;
    protected String taskTableName = "crawlerTaskTable";
    protected List<EntityModel> writeBackEntityList;

    private Lock myLock;

    protected RulerBase() {

        try {
            maxTaskQueueLength = Integer.parseInt(ConfigUtils.getResourceBundle().getString("MAX_TASK_QUEUE_LENGTH"));
            taskTableName = ConfigUtils.getResourceBundle().getString("CRAWLER_TASK_TABLE");
        } catch (NumberFormatException e) {
            maxTaskQueueLength = 100;
            e.printStackTrace();
        }

        //currentTaskQueueLength = TaskQueue.queueLenth();
        inQueueTaskIdArray = new String[(int) maxTaskQueueLength];
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

    public List<String> getInQueueTaskIdArray() {
        List<String> inQueue = new ArrayList<>();
        String new_s;

        try {
            myLock.lock();
            int size = inQueueTaskIdArray.length;
            for (int i = 0; i < size; i++) {

                if(inQueueTaskIdArray[i] == null)
                    continue;
                new_s = new String(inQueueTaskIdArray[i]);

                inQueue.add(new_s);
            }

        } catch (Exception e) {
            System.out.println("Exception: at RulerBase.java, method getInQueueTaskIdArray().");
            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return inQueue;

    }

    public void removeInQueueTaskId(String taskId) {

        try {
            myLock.lock();
            int size = inQueueTaskIdArray.length;
            int i;
            for (i = 0; i < size; i++) {

                if (inQueueTaskIdArray[i] == null)
                    continue;
                if (inQueueTaskIdArray[i].equals(taskId)) {
                    inQueueTaskIdArray[i] = null;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: at RulerBase.java, method removeInQueueTaskId(...).");
            e.printStackTrace();
        } finally {
            myLock.unlock();
        }
    }

    public boolean isInQueueTaskIdHaveThis(String taskId) {

        try {
            myLock.lock();

            int size = inQueueTaskIdArray.length;
            for (int i = 0; i < size; i++) {
                if (inQueueTaskIdArray[i] == null)
                    continue;
                if (inQueueTaskIdArray[i].equals(taskId))
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

    public void addTaskIdToInQueue(String taskId) {

        if (!isInQueueTaskIdHaveThis(taskId)) {

            myLock.lock();

            try {
                int size = inQueueTaskIdArray.length;
                for (int i = 0; i < size; i++) {

                    if (inQueueTaskIdArray[i] == null) {
                        inQueueTaskIdArray[i] = taskId;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception: at RulerBase.java, method addTaskIdToInQueue(...).");
                e.printStackTrace();
            } finally {
                myLock.unlock();
            }
        }
    }
}
