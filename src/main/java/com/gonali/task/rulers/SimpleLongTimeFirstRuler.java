package com.gonali.task.rulers;


import com.gonali.task.message.codes.TaskStatus;
import com.gonali.task.model.EntityModel;
import com.gonali.task.model.TaskModel;
import com.gonali.task.rulers.base.RulerBase;
import com.gonali.task.scheduler.TaskScheduler;
import com.gonali.task.model.HeartbeatMsgModel;
import com.gonali.task.redisQueue.TaskQueue;
import com.gonali.task.scheduler.CurrentTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class SimpleLongTimeFirstRuler extends RulerBase {


    public SimpleLongTimeFirstRuler() {
        super();
    }

    @Override
    public void writeBack(TaskScheduler scheduler) {

        //List<TaskModel> currentTasks = scheduler.getCurrentTasks().getCrawledTask();

        try {

            /*for (TaskModel t : currentTasks)
                addToWriteBack(t);*/

            for (EntityModel m : writeBackEntityList) {
                taskModelDao.update(taskTableName, m);
                //            writeBackEntityList.remove(m);
            }

            writeBackEntityList = new ArrayList<>();

        } catch (Exception e) {
            System.out.println("Exception: at SimpleLongTimeFirstRuler.java, method writeBack().");
            e.printStackTrace();
        }

    }

    @Override
    public CurrentTask doSchedule(TaskScheduler scheduler) {

        currentTasks = scheduler.getCurrentTasks();
        List<HeartbeatMsgModel> heartbeatList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();
        TaskModel task;

        if (!scheduler.getRuntimeControlMsg().isTaskRunning()){

            while((task = getTask()) != null){

                task.setTaskStatus(TaskStatus.UNCRAWL);
                addToWriteBack(task);
            }
            currentTasks.setHeartbeatList(heartbeatList);
            return currentTasks;
        }


        //cleanInQueueTaskId();

        if (getCurrentTaskQueueLength() < 2 * scheduler.getCurrentTasks().getTaskNumber())
            updateTaskQueue();

        while (currentTasks.isHaveFinishedTask()) {

            task = getTask();

            if (task == null)
                break;

            task.setTaskStartTime(System.currentTimeMillis());
            task.setTaskStatus(TaskStatus.UNCRAWL);
            currentTasks.addTask(task);
            removeInQueueTaskIdAndName(task.getTaskId());
        }

        //cleanInQueueTaskId();

        currentTasks.setHeartbeatList(heartbeatList);

        return currentTasks;
    }

    /* private void cleanInQueueTaskId() {

        List<TaskModel> taskModelList = currentTasks.getCurrentTaskElements();
        for (TaskModel t : taskModelList) {

            if (isInQueueTaskIdHaveThis(t.getTaskId()))
                removeInQueueTaskIdAndName(t.getTaskId());
        }

    }*/

    private void updateTaskQueue() {

        List<EntityModel> modelList = taskModelDao.selectAll(taskTableName);
        List<EntityModel> toQueue = new ArrayList<>();
        int size = modelList.size();
        for (int i = 0; i < size; i++) {

            /*if (!isInQueueTaskIdHaveThis(((TaskModel) modelList.get(i)).getTaskId())) {
                toQueue.add(modelList.get(i));
            }*/

            if (!isInQueueTaskIdHaveThis(((TaskModel) modelList.get(i)).getTaskId()) &&
                    ((TaskModel) modelList.get(i)).getTaskStatus() == TaskStatus.UNCRAWL) {
                toQueue.add(modelList.get(i));
            }
        }

        TaskModel[] sortedModels = sortByTimeAesc(toQueue);

        int length = sortedModels.length;
        for (int i = 0; i < length; i++) {

            if (getCurrentTaskQueueLength() < getMaxTaskQueueLength()) {

                sortedModels[i].setTaskStatus(TaskStatus.INQUEUE);
                addTaskIdAndNameToInQueue(sortedModels[i].getTaskId(), sortedModels[i].getTaskName());
                TaskQueue.pushCrawlerTaskQueue(sortedModels[i]);
                addToWriteBack(sortedModels[i]);
                //getCurrentTaskQueueLength();
            } else break;
        }

        getCurrentTaskQueueLength();
        //cleanInQueueTaskId();
    }


    private TaskModel[] sortByTimeAesc(List<EntityModel> modelList) {

        int size = modelList.size();
        TaskModel[] models = new TaskModel[size];
        TaskModel temp;

        for (int i = 0; i < size; i++)
            models[i] = (TaskModel) modelList.get(i);

        int k = 0, l = size - 1;

        while (k < size) {
            while (k != l) {
                if (models[k].getTaskStopTime() > models[l].getTaskStopTime()) {

                    temp = models[k];
                    models[k] = models[l];
                    models[l] = temp;
                }
                l--;
            }
            l = size - 1;
            k++;
        }
        return models;
    }


    private TaskModel getTask() {

        currentTaskQueueLength = TaskQueue.queueLenth();
        TaskModel task;
        if (currentTaskQueueLength > 0) {

            task = TaskQueue.popCrawlerTaskQueue();
            if (task == null) {
                System.out.println("ERROR: Scheduler crashed !!!! at SimpleLongTimeFirstRuler.java, method getTask().");
                return null;
            }
            currentTaskQueueLength = TaskQueue.queueLenth();

        } else {

            return null;
        }

        return task;
    }


}
