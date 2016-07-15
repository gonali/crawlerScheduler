package com.gonali.task.dao;


import com.gonali.task.dao.client.MysqlClient;
import com.gonali.task.model.EntityModel;
import com.gonali.task.utils.LoggerUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by TianyuanPan on 6/3/16.
 */
public class TaskUserModelDao extends LoggerUtils implements QueryDao {


    private MysqlClient mysqlClient;

    public TaskUserModelDao() {

        mysqlClient = new MysqlClient();
    }


    private List<EntityModel> boxingObject(ResultSet resultSet) throws SQLException {

        return null;
    }


    @Override
    public int insert(String tableName, EntityModel model) {
        return 0;
    }

    @Override
    public int update(String tableName, EntityModel model) {
        return 0;
    }

    @Override
    public int delete(String tableName, EntityModel model) {
        return 0;
    }

    @Override
    public List<EntityModel> selectWhere(String tableName, String whereStatement) {
        return null;
    }

    @Override
    public List<EntityModel> selectAll(String tableName) {
        return null;
    }

    @Override
    public long countAll(String tableName) {
        return 0;
    }

    @Override
    public long countWhere(String tableName, String whereStatement) {
        return 0;
    }
}
