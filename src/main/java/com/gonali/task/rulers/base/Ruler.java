package com.gonali.task.rulers.base;


import com.gonali.task.scheduler.CurrentTask;
import com.gonali.task.scheduler.TaskScheduler;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public interface Ruler {

    public void writeBack(TaskScheduler scheduler);
    public CurrentTask doSchedule(TaskScheduler scheduler);
}
