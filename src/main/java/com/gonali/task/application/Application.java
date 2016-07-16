package com.gonali.task.application;

import com.gonali.task.deamon.AppMainEntry;
import com.gonali.task.rulers.SimpleLongTimeFirstRuler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@SpringBootApplication
public class Application {

    //public static AppMainEntry myApp;

    public static void main(String[] args) {

        AppMainEntry myApp = new AppMainEntry();
        myApp.setRuler(new SimpleLongTimeFirstRuler());
        Thread myThread = new Thread(myApp);
        myThread.setDaemon(false);

        SpringApplication.run(Application.class, args);

        myThread.run();
    }

}


