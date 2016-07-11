package com.gonali.task.dao.deamon;


import com.gonali.task.dao.rulers.base.RulerBase;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class AppMainEntry {

    private static TaskDeamon taskDeamon;


    static {

        taskDeamon = TaskDeamon.createDeamon();
    }


    public void appStart(RulerBase ruler) {

        Thread taskDeamon = new Thread(AppMainEntry.taskDeamon.setRuler(ruler));
        taskDeamon.setDaemon(false);
        taskDeamon.start();

        System.out.println("Hi! Scheduler AppDeamon!");
    }


}
