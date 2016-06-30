package com.gonali.task.deamon;


import com.gonali.task.rulers.base.RulerBase;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class MainDeamon {

    private static volatile MainDeamon mainDeamon;
    //private HearbeatDeamon hearbeatDeamon;
    private TaskDeamon taskDeamon;
    //private HtmlDeamon htmlDeamon;


    private MainDeamon() {

        //hearbeatDeamon = HearbeatDeamon.createDeamon();
        taskDeamon = TaskDeamon.createDeamon();
        //htmlDeamon = HtmlDeamon.createDeamon();
    }


    public static MainDeamon createDeamon() {

        if (mainDeamon == null)
            mainDeamon = new MainDeamon();

        return mainDeamon;

    }


    public void appStart(RulerBase ruler) {


        // Thread heartbeatDeamon = new Thread(this.hearbeatDeamon);
        Thread taskDeamon = new Thread(this.taskDeamon.setRuler(ruler));
        //Thread htmlDeamon = new Thread(this.htmlDeamon);

        // heartbeatDeamon.start();
        taskDeamon.setDaemon(false);
        //htmlDeamon.setDaemon(false);
        taskDeamon.start();
        //htmlDeamon.start();


        //  System.out.println("HeartbeatServer Deamon Id : " + heartbeatDeamon.getId());
        System.out.println("HtmlServer      Deamon Id : "/* + htmlDeamon.getId()*/);
        System.out.println("Hi! Scheduler MainDeamon!");
    }
}
