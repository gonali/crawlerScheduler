package com.gonali.task.dao;

import com.gonali.task.dao.client.MysqlClient;
import com.gonali.task.model.EntityModel;
import com.gonali.task.model.TaskConfigModel;
import com.gonali.task.model.TaskSlaveModel;
import com.gonali.task.model.fields.TaskSlaveTableField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 7/1/16.
 */
public class TaskSlaveModelDao implements QueryDao {

    private MysqlClient mysqlClient;

    public TaskSlaveModelDao() {

        mysqlClient = new MysqlClient();
    }


    private List<EntityModel> boxingObject(ResultSet resultSet) throws Exception {

        List<EntityModel> entityList = new ArrayList<>();
        TaskSlaveModel slave;

        while (resultSet.next()) {
            slave = new TaskSlaveModel();

            slave.setSlaveId(resultSet.getString(TaskSlaveTableField.PK));
            slave.setSlaveUsername(resultSet.getString(TaskSlaveTableField.slaveUsername));
            slave.setSlavePassword(resultSet.getString(TaskSlaveTableField.slavePassword));
            slave.setSlaveIp(resultSet.getString(TaskSlaveTableField.slaveIp));
            slave.setSlaveSshPort(resultSet.getInt(TaskSlaveTableField.slaveSshPort));
            slave.setSlaveAppPath(resultSet.getString(TaskSlaveTableField.slaveAppPath));

            entityList.add(slave);
        }


        return entityList;

    }


    @Override
    public int insert(String tableName, EntityModel model) {

        int ret = 0;

        try {
            mysqlClient.getConnection();
            ret = mysqlClient.excuteUpdateSql(model.insertSqlBuilder(tableName, model));

        } catch (SQLException e) {

            e.printStackTrace();
        }finally {

            mysqlClient.closeConnection();
        }
        return ret;
    }

    @Override
    public int update(String tableName, EntityModel model) {
        try {
            mysqlClient.getConnection();
            return mysqlClient.excuteUpdateSql(model.updateSqlBuilder(tableName, model));
        } catch (SQLException e) {

            e.printStackTrace();
        }finally {
            mysqlClient.closeConnection();
        }
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
        try {
            String sql = "SELECT * FROM " + tableName + ";";
            mysqlClient.getConnection();
            ResultSet rs = mysqlClient.executeQuerySql(sql);
            return boxingObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            mysqlClient.closeConnection();
        }
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
