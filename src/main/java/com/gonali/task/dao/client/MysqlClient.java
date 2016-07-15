package com.gonali.task.dao.client;


import com.gonali.task.utils.ConfigUtils;
import com.gonali.task.utils.LoggerUtils;
import com.gonali.task.utils.MySqlPoolUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class MysqlClient extends LoggerUtils {

    private Statement myStatement;
    private ConfigUtils configUtils;
    private MySqlPoolUtils pool;
    private boolean connOpen;
    private Connection connection;


    private void setConnOpen(boolean connOpen) {
        this.connOpen = connOpen;
    }

    public MysqlClient() {

        this.configUtils = ConfigUtils.getConfigUtils();
        this.pool = MySqlPoolUtils.getMySqlPoolUtils(configUtils);
        this.myStatement = null;

    }

    public boolean isConnOpen() {
        return connOpen;
    }

    public Connection getConnection() {

        try {
            logger.debug("get Mysql connection ...");
            this.connection = this.pool.getConnection();
            this.myStatement = this.connection.createStatement();
            if (this.connection != null)
                this.setConnOpen(true);

        } catch (Exception ex) {

            logger.error("get mysql connection error!! Exception Message: " + ex.getMessage());
            ex.printStackTrace();
            this.setConnOpen(false);
            return null;
        }

        return this.connection;
    }


    public void closeConnection() {

        try {

            this.pool.releaseConnection(this.connection);

            this.setConnOpen(false);

        } catch (Exception ex) {

            this.setConnOpen(false);
            logger.error("connection.close() exception!! Message:" + ex.getMessage());
            ex.printStackTrace();

        }
    }


    public ResultSet executeQuerySql(String sql) throws SQLException {

        return this.myStatement.executeQuery(sql);
    }

    public int excuteUpdateSql(String sql) throws SQLException {

        return this.myStatement.executeUpdate(sql);
    }
}
