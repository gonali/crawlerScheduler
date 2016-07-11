package com.gonali.task.dao.model;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskUserModel implements EntityModel {

    private String userId = "";
    private String userAppkey = "";
    private String userDescription = "";

    public TaskUserModel() {

    }

    public TaskUserModel(String userId) {

        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAppkey() {
        return userAppkey;
    }

    public void setUserAppkey(String userAppkey) {
        this.userAppkey = userAppkey;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }


    @Override
    public String getPrimaryKey() {
        return this.userId;
    }

    public String insertSqlBuilder(String tableName, EntityModel model) {
        return null;
    }

    public String updateSqlBuilder(String tableName, EntityModel model) {
        return null;
    }

    public String subUpdateSqlBuilder(String tableName, EntityModel model, String... fields) {
        return null;
    }
}
