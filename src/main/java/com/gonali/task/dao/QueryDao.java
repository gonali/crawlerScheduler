package com.gonali.task.dao;


import com.gonali.task.model.EntityModel;

import java.util.List;

/**
 * Created by TianyuanPan on 6/3/16.
 */
public interface QueryDao {

    public int insert(String tableName, EntityModel model);

    public int update(String tableName, EntityModel model);

    public int delete(String tableName, EntityModel model);

    public List<EntityModel> selectWhere(String tableName, String whereStatement);

    public List<EntityModel> selectAll(String tableName);

    public long countAll(String tableName);

    public long countWhere(String tableName, String whereStatement);
}
