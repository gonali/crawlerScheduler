package com.gonali.task.dao.rulers.base;


import com.gonali.task.dao.scheduler.CurrentTask;
import com.gonali.task.dao.scheduler.TaskScheduler;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public interface Ruler {

    public void writeBack(TaskScheduler scheduler);
    public CurrentTask doSchedule(TaskScheduler scheduler);
}
