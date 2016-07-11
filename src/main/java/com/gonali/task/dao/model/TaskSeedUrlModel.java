package com.gonali.task.dao.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskSeedUrlModel implements Serializable{

    private List<String>  seedUrlList;
    private int           size;



    public TaskSeedUrlModel(){

        seedUrlList = new ArrayList<>();
        size = 0;
    }

    public TaskSeedUrlModel(String seedUrlJson){


        try {

                seedUrlList = JSON.parseObject(seedUrlJson, List.class);
                size = seedUrlList.size();

        }catch (Exception ex){

            seedUrlList = new ArrayList<>();
            size = seedUrlList.size();
            ex.printStackTrace();
        }

    }

    public List<String> getSeedUrlList() {
        return seedUrlList;
    }

    public String getSeedurlJsonString(){

        try {

            return JSON.toJSONString(this.seedUrlList);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    public void setSeedUrlList(List<String> seedUrlList) {

        this.seedUrlList = seedUrlList;
    }

    public void setSeedUrlJsonString(String seedUrlJsonString) {

        try {

            this.seedUrlList = JSON.parseObject(seedUrlJsonString, List.class);

        }catch (Exception ex){

            ex.printStackTrace();
        }
    }

    public int getSize() {

        return size;
    }

}
