package com.gonali.task.model;

import java.io.Serializable;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public interface EntityModel extends Serializable {

    public String getPrimaryKey();

    public String insertSqlBuilder(String tableName, EntityModel model);

    public String updateSqlBuilder(String tableName, EntityModel model);

    public String subUpdateSqlBuilder(String tableName, EntityModel model, String... fields);

}
