package com.gonali.task.deamon;


import com.gonali.task.scheduler.TaskScheduler;
import com.gonali.task.rulers.base.RulerBase;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class TaskDeamon implements Runnable {

    private static TaskDeamon taskDeamon;
    private TaskScheduler scheduler;

    private TaskDeamon() {

        scheduler = TaskScheduler.createTaskScheduler();
    }


    public TaskDeamon setRuler(RulerBase ruler) {

        scheduler.registerRuler(ruler);
        return this;
    }

    @Override
    public void run() {

        scheduler.schedulerStart();
    }


    public static TaskDeamon createDeamon() {

        if (taskDeamon == null)
            taskDeamon = new TaskDeamon();

        return taskDeamon;
    }

}
