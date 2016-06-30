package com.gonali.task.dao;

import com.gonali.task.dao.client.MysqlClient;
import com.gonali.task.model.EntityModel;
import com.gonali.task.model.TaskConfigModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/30/16.
 */
public class TaskConfigModelDao implements QueryDao {

    private MysqlClient mysqlClient;


    public TaskConfigModelDao(){

        mysqlClient = new MysqlClient();
    }

    private List<EntityModel> boxingObject (ResultSet resultSet) throws Exception{

        List<EntityModel> entityModelList = new ArrayList<>();
        TaskConfigModel taskConfigModel;

        while (resultSet.next()){

            taskConfigModel = new TaskConfigModel();

            taskConfigModel.setConfigId(resultSet.getInt("configId"));
            taskConfigModel.setRedisHost(resultSet.getString("redisHost"));
            taskConfigModel.setRedisPort(resultSet.getInt("redisPort"));
            taskConfigModel.setMaxTaskQueueSize(resultSet.getInt("maxTaskQueueSize"));
            taskConfigModel.setMaxTaskRun(resultSet.getInt("maxTaskRun"));
            taskConfigModel.setMaxHeartbeatTimeoutCount(resultSet.getInt("maxHeartbeatTimeoutCount"));
            taskConfigModel.setSlaveHeartbeatInterval(resultSet.getInt("slaveHeartbeatInterval"));
            taskConfigModel.setSlaveAppScript(resultSet.getString("slaveAppScript"));

            entityModelList.add(taskConfigModel);
        }


        return entityModelList;

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

        String sql = "SELECT * FROM " +  tableName + ";";

        ResultSet resultSet;
        try {
            resultSet = mysqlClient.executeQuerySql(sql);
            return boxingObject(resultSet);

        } catch (SQLException e) {

            e.printStackTrace();

        }catch (Exception ex){

            ex.getMessage();
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
