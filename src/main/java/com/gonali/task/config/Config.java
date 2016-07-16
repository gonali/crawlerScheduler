package com.gonali.task.config;

import com.gonali.task.dao.TaskConfigModelDao;
import com.gonali.task.model.TaskConfigModel;

import java.util.ResourceBundle;

/**
 * Created by TianyuanPan on 6/30/16.
 */
public class Config {

    private static ResourceBundle resourceBundle;

    static {

        try {
            resourceBundle = ResourceBundle.getBundle("config");
            mysqlHost = resourceBundle.getString("MYSQL_HOSTNAME");
            mysqlPort = Integer.parseInt(resourceBundle.getString("MYSQL_PORT"));
            mysqlDbName = resourceBundle.getString("MYSQL_DBNAME");
            mysqlUsername = resourceBundle.getString("MYSQL_USER");
            mysqlPassword = resourceBundle.getString("MYSQL_PASSWORD");
            taskConfigTable = resourceBundle.getString("CRAWLER_CONF_TABLE");
            taskTable = resourceBundle.getString("CRAWLER_TASK_TABLE");
            taskSlaveTable = resourceBundle.getString("CRAWLER_SLAVE_TABLE");
            taskUserTable = resourceBundle.getString("CRAWLER_USER_TABLE");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private static String mysqlHost;
    private static int mysqlPort;
    private static String mysqlDbName;
    private static String mysqlUsername;
    private static String mysqlPassword;
    private static String taskConfigTable;
    private static String taskTable;
    private static String taskSlaveTable;
    private static String taskUserTable;

    private static TaskConfigModel taskConfig;


    private Config() {
    }

    public static Config getConfig() {

        try {
            if (taskConfig == null)
                taskConfig = (TaskConfigModel) (new TaskConfigModelDao().selectAll(taskConfigTable).get(0));
        } catch (Exception e) {

            e.printStackTrace();
        }

        return new Config();
    }


    public String getMysqlHost() {
        return mysqlHost;
    }

    public void setMysqlHost(String mysqlHost) {
        Config.mysqlHost = mysqlHost;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public void setMysqlPort(int mysqlPort) {
        Config.mysqlPort = mysqlPort;
    }

    public String getMysqlDbName() {
        return mysqlDbName;
    }

    public static void setMysqlDbName(String mysqlDbName) {
        Config.mysqlDbName = mysqlDbName;
    }

    public static String getMysqlUsername() {
        return mysqlUsername;
    }

    public static void setMysqlUsername(String mysqlUsername) {
        Config.mysqlUsername = mysqlUsername;
    }

    public static String getMysqlPassword() {
        return mysqlPassword;
    }

    public static void setMysqlPassword(String mysqlPassword) {
        Config.mysqlPassword = mysqlPassword;
    }

    public static String getTaskConfigTable() {
        return taskConfigTable;
    }

    public static void setTaskConfigTable(String taskConfigTable) {
        Config.taskConfigTable = taskConfigTable;
    }

    public static String getTaskTable() {
        return taskTable;
    }

    public static void setTaskTable(String taskTable) {
        Config.taskTable = taskTable;
    }

    public static String getTaskSlaveTable() {
        return taskSlaveTable;
    }

    public static void setTaskSlaveTable(String taskSlaveTable) {
        Config.taskSlaveTable = taskSlaveTable;
    }

    public static String getTaskUserTable() {
        return taskUserTable;
    }

    public static void setTaskUserTable(String taskUserTable) {
        Config.taskUserTable = taskUserTable;
    }

    public static TaskConfigModel getTaskConfig() {
        return taskConfig;
    }

    public static void setTaskConfig(TaskConfigModel taskConfig) {
        Config.taskConfig = taskConfig;
    }
}
