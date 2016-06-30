package com.gonali.task.deamon;


import com.gonali.task.rulers.SimpleLongTimeFirstRuler;
import com.gonali.task.rulers.base.RulerBase;

import java.lang.management.ManagementFactory;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class AppMainEntry {

    private static MainDeamon mainDeamon;


    static {

        mainDeamon = MainDeamon.createDeamon();
    }


    public static void main(String[] args) {

        System.out.println("\n\n======>>> task scheduler PID: " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0] + " <<<======\n");
        RulerBase ruler;
        ruler = new SimpleLongTimeFirstRuler();
        mainDeamon.appStart(ruler);

    }
}
