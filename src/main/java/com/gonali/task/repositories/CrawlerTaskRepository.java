package com.gonali.task.repositories;

import com.gonali.task.entityModel.CrawlerTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by TianyuanPan on 6/29/16.
 */
public interface CrawlerTaskRepository extends CrudRepository<CrawlerTask, String> {


    CrawlerTask findByTaskId(String taskId);

    List<CrawlerTask> findByUserId(String userId);


}
