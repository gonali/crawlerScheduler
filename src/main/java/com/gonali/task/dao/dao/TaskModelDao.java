package com.gonali.task.dao.dao;



import com.gonali.task.dao.dao.client.MysqlClient;
import com.gonali.task.dao.model.*;
import com.gonali.task.dao.model.fields.TaskConfigModelTableField;
import com.gonali.task.dao.utils.LoggerUtils;
import com.gonali.task.dao.message.codes.TaskStatus;
import com.gonali.task.dao.message.codes.TaskType;
import com.gonali.task.dao.model.fields.TaskModeTableField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/3/16.
 */
public class TaskModelDao extends LoggerUtils implements QueryDao {


    private MysqlClient mysqlClient;

    public TaskModelDao() {

        mysqlClient = new MysqlClient();
    }


    private List<EntityModel> boxingObject(ResultSet resultSet) throws SQLException {

        List<EntityModel> modelList = new ArrayList<>();
        TaskModel model;

        while (resultSet.next()) {

            model = new TaskModel();

            model.setTaskId(resultSet.getString(TaskModeTableField.taskId));
            model.setUserId(resultSet.getString(TaskModeTableField.userId));

            String taskType = resultSet.getNString(TaskModeTableField.taskType);
            switch (taskType) {
                case "TOPIC":
                    model.setTaskType(TaskType.TOPIC);
                    break;
                case "WHOLESITE":
                    model.setTaskType(TaskType.WHOLESITE);
                    break;
                default:
                    model.setTaskType(TaskType.UNSET);
                    break;
            }

            model.setTaskName(resultSet.getString(TaskModeTableField.taskName));
            model.setTaskRemark(resultSet.getString(TaskModeTableField.taskRemark));


            String seedUrls;
            try {

                seedUrls = resultSet.getString(TaskModeTableField.taskSeedUrl);

            } catch (SQLException e) {

                e.printStackTrace();

                seedUrls = "[\"seedUrl\",\"seedUrl\"]";

            }

            model.setTaskSeedUrl(new TaskSeedUrlModel(seedUrls));
            model.setTaskCrawlerDepth(resultSet.getInt(TaskModeTableField.taskCrawlerDepth));
            model.setTaskDynamicDepth(resultSet.getInt(TaskModeTableField.taskDynamicDepth));
            model.setTaskPass(resultSet.getInt(TaskModeTableField.taskPass));
            model.setTaskWeight(resultSet.getInt(TaskModeTableField.taskWeight));
            model.setTaskStartTime(resultSet.getTimestamp(TaskModeTableField.taskStartTime).getTime());
            model.setTaskRecrawlerDays(resultSet.getInt(TaskModeTableField.taskRecrawlerDays));
            model.setTaskTemplatePath(resultSet.getString(TaskModeTableField.taskTemplatePath));
            model.setTaskTagPath(resultSet.getString(TaskModeTableField.taskTagPath));
            model.setTaskSeedPath(resultSet.getString(TaskModeTableField.taskSeedPath));
            model.setTaskConfigPath(resultSet.getString(TaskModeTableField.taskConfigPath));
            model.setTaskClickRegexPath(resultSet.getString(TaskModeTableField.taskClickRegexPath));
            model.setTaskProtocolFilterPath(resultSet.getString(TaskModeTableField.taskProtocolFilterPath));
            model.setTaskSuffixFilterPath(resultSet.getString(TaskModeTableField.taskSuffixFilterPath));
            model.setTaskRegexFilterPath(resultSet.getString(TaskModeTableField.taskRegexFilterPath));

            switch (resultSet.getNString(TaskModeTableField.taskStatus)) {
                case "CRAWLING":
                    model.setTaskStatus(TaskStatus.CRAWLING);
                    break;
                case "CRAWLED":
                    model.setTaskStatus(TaskStatus.CRAWLED);
                    break;
                case "EXCEPTIOSTOP":
                    model.setTaskStatus(TaskStatus.EXCEPTIOSTOP);
                    break;
                case "INQUEUE":
                    model.setTaskStatus(TaskStatus.INQUEUE);
                    break;
                case "UNCRAWL":
                    model.setTaskStatus(TaskStatus.UNCRAWL);
                    break;
                default:
                    break;
            }

            model.setTaskDeleteFlag(resultSet.getBoolean(TaskModeTableField.taskDeleteFlag));
            model.setTaskSeedAmount(resultSet.getInt(TaskModeTableField.taskSeedAmount));
            model.setTaskSeedImportAmount(resultSet.getInt(TaskModeTableField.taskSeedImportAmount));
            model.setTaskCompleteTimes(resultSet.getInt(TaskModeTableField.taskCompleteTimes));
            model.setTaskInstanceThreads(resultSet.getInt(TaskModeTableField.taskInstanceThreads));
            model.setTaskNodeInstances(resultSet.getInt(TaskModeTableField.taskNodeInstances));
            model.setTaskStopTime(resultSet.getTimestamp(TaskModeTableField.taskStopTime).getTime());
            model.setTaskCrawlerAmountInfo(new TaskCrawlerAmountInfoModel(resultSet.getNString(TaskModeTableField.taskCrawlerAmountInfo)));
            model.setTaskCrawlerInstanceInfo(new TaskCrawlerInstanceInfoModel().setCrawlerInstanceInfo(resultSet.getNString(TaskModeTableField.taskCrawlerInstanceInfo)));


            modelList.add(model);

        }

        return modelList;
    }

    public int softDeleteById(String tableName, EntityModel model){

        String sql = "UPDATE " + tableName + " SET "+ TaskModeTableField.taskDeleteFlag +" = true WHERE " + TaskModeTableField.PK + " = '" + model.getPrimaryKey() + "'";

        if (this.mysqlClient.getConnection() == null)
            return 0;

        int ret = 0;
        try {

            ret = this.mysqlClient.excuteUpdateSql(sql);

            return ret;

        } catch (Exception ex) {
            logger.warn("delete exception!!! SQL: " + sql);
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }

        return 0;
    }

    @Override
    public int insert(String tableName, EntityModel model) {

        if (this.mysqlClient.getConnection() == null)
            return 0;

        String sql = null;

        int ret = 0;

        try {

            sql = model.insertSqlBuilder(tableName, model);
            ret = this.mysqlClient.excuteUpdateSql(sql);

            return ret;

        } catch (Exception ex) {
            logger.warn("insert exception !!! SQL: " + sql);
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }

        return 0;
    }

    @Override
    public int update(String tableName, EntityModel model) {

        if (this.mysqlClient.getConnection() == null)
            return 0;

        String sql = null;
        int ret = 0;
        try {

            sql = model.updateSqlBuilder(tableName, model);

            ret = this.mysqlClient.excuteUpdateSql(sql);

            return ret;

        } catch (Exception ex) {
            logger.warn("update exception!!! SQL: " + sql);
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }

        return 0;
    }

    @Override
    public int delete(String tableName, EntityModel model) {

        String sql = "DELETE FROM " + tableName + " WHERE " + TaskModeTableField.PK + " = '" + model.getPrimaryKey() + "'";

        if (this.mysqlClient.getConnection() == null)
            return 0;

        int ret = 0;
        try {

            ret = this.mysqlClient.excuteUpdateSql(sql);

            return ret;

        } catch (Exception ex) {
            logger.warn("delete exception!!! SQL: " + sql);
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }

        return 0;
    }


    public List<EntityModel> selectByUserId(String tableName, String userId) {


        String sql = "SELECT * FROM " + tableName + " WHERE " + TaskModeTableField.userId + " = '" + userId + "'";
        ResultSet resultSet;
        List<EntityModel> entityModelList = new ArrayList<>();

        if (this.mysqlClient.getConnection() == null) {
            return null;
        }

        try {

            resultSet = this.mysqlClient.executeQuerySql(sql);

            entityModelList = boxingObject(resultSet);

        } catch (Exception ex) {
            logger.warn("selectByUserId exception!!! SQL: " + sql);
            ex.printStackTrace();

            return null;

        } finally {

            this.mysqlClient.closeConnection();
        }

        return entityModelList;
    }


    public EntityModel selectByTaskId(String tableName, String taskId) {

        String sql = "SELECT * FROM " + tableName +
                " WHERE " + TaskModeTableField.taskId + " = '" + taskId + "'";
        ResultSet resultSet;
        List<EntityModel> entityModelList;

        if (this.mysqlClient.getConnection() == null)
            return null;

        try {

            resultSet = this.mysqlClient.executeQuerySql(sql);

            entityModelList = boxingObject(resultSet);

            return entityModelList.get(0);

        } catch (Exception ex) {
            logger.warn("selectByTaskId exception!!! SQL: " + sql);
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }
        return null;
    }

    @Override
    public List<EntityModel> selectWhere(String tableName, String whereStatement) {

        String sql = "SELECT * FROM " + tableName + " WHERE " + whereStatement;

        ResultSet resultSet;
        List<EntityModel> entityModelList;

        if (this.mysqlClient.getConnection() == null)
            return null;

        try {

            resultSet = this.mysqlClient.executeQuerySql(sql);

            entityModelList = boxingObject(resultSet);

            return entityModelList;

        } catch (Exception ex) {
            logger.warn("selectWhere exception!!! SQL: " + sql);
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }
        return null;
    }

    @Override
    public List<EntityModel> selectAll(String tableName) {

        String sql = "SELECT * FROM " + tableName;

        ResultSet resultSet;

        if (this.mysqlClient.getConnection() == null)
            return null;

        try {

            resultSet = this.mysqlClient.executeQuerySql(sql);

            return boxingObject(resultSet);

        } catch (Exception ex) {
            logger.warn("selectAll exception!!! SQL: " + sql);
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
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
