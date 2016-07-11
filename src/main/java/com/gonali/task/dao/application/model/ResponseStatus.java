package com.gonali.task.dao.application.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by TianyuanPan on 7/6/16.
 */
public class ResponseStatus {

    private boolean status;
    private boolean isRedirect;
    private String location;

    public ResponseStatus(){

        status = false;
        isRedirect = false;
        location = "/login.html";
    }

    public ResponseStatus setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public ResponseStatus setLocation(String location) {
        this.location = location;
        return this;
    }

    public ResponseStatus setIsRedirect(boolean isRedirect) {
        this.isRedirect = isRedirect;
        return this;
    }

    @Override
    public String toString(){

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("status", status);
        jsonObject.put("isRedirect", isRedirect);
        jsonObject.put("location", location);


        return jsonObject.toJSONString();

    }
}
