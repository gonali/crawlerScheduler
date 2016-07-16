package com.gonali.task.deamon;


import com.gonali.task.rulers.base.RulerBase;
import com.gonali.task.scheduler.TaskScheduler;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class AppMainEntry implements Runnable{


    private static TaskScheduler scheduler;

    static {

        scheduler = TaskScheduler.createTaskScheduler();
    }


    public AppMainEntry setRuler(RulerBase ruler) {

        scheduler.registerRuler(ruler);
        return this;
    }


    @Override
    public void run() {

        scheduler.schedulerStart();
    }
}
