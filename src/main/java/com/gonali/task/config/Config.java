package com.gonali.task.config;

import com.gonali.task.utils.ConfigUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by TianyuanPan on 6/30/16.
 */
public class Config {

    private static Map<String, Object> configItems;

    public static void initConfig() {

        configItems = new HashMap<>();
        ResourceBundle rsb =  ConfigUtils.getResourceBundle();
    }

    public static Object getValue(String key) {

        return configItems.get(key);
    }
}
