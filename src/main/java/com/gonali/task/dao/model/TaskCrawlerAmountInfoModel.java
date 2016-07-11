package com.gonali.task.dao.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskCrawlerAmountInfoModel implements Serializable {

    private Map<String, Integer> amountMap;
    private List<AmountInfo> amountInfoList;


    private class AmountInfo {

        private int times;
        private int amount;

        public AmountInfo() {
        }

        public AmountInfo(int times, int amount) {
            this.times = times;
            this.amount = amount;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }


    private void sync(){

        try {

//            amountMap = new HashMap<String, Integer>();
//            for(int i = 0; i < amountInfoList.size(); i++)
//                amountMap.put("times-" + amountInfoList.get(i).getTimes(),
//                        amountInfoList.get(i).getAmount());

        }catch (Exception ex){

            ex.printStackTrace();
        }

    }

    public TaskCrawlerAmountInfoModel() {

        amountMap = new HashMap<String, Integer>();
        amountInfoList = new ArrayList<AmountInfo>();
    }

    public TaskCrawlerAmountInfoModel(String crawlerAmountInfoJson) {

        amountMap = new HashMap<String, Integer>();
        try {

            amountInfoList = JSON.parseObject(crawlerAmountInfoJson, List.class);
            sync();

        }catch (Exception ex){

            ex.printStackTrace();
        }
    }

    public Map<String, Integer> getAmountMap() {
        return amountMap;
    }


    public List<AmountInfo> getAmountInfoList() {
        return amountInfoList;
    }


    public int getAmountByTimes(int times){

        return this.amountMap.get("times-" + times);
    }

    public void setAmountInfoList(String crawlerAmountInfoJson) {

        amountMap = new HashMap<String, Integer>();
        try {

            amountInfoList = JSON.parseObject(crawlerAmountInfoJson, List.class);
            sync();

        }catch (Exception ex){

            ex.printStackTrace();
        }
    }

    public String getCrawlerAmountInfoJsonString(){

        return JSON.toJSONString(this.amountInfoList);
    }
}
