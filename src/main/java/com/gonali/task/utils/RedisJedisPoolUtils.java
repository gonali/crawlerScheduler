package com.gonali.task.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.Serializable;


/**
 * Created by 黄海 on 15-12-30.
 */

public class RedisJedisPoolUtils implements Serializable {

    private static JedisPool pool;
    private Jedis jedis;


    private RedisJedisPoolUtils() {

        jedis = null;
    }


    private static void makepool() throws IOException {

        if (pool == null) {

            String redisHost = "127.0.0.1";//ConfigUtils.getResourceBundle().getString("REDIS_HOSTNAME");
            int redisPort = 6379;//Integer.parseInt(ConfigUtils.getResourceBundle().getString("REDIS_PORT"));
            JedisPoolConfig conf = new JedisPoolConfig();
            conf.setMaxTotal(-1);
            conf.setMaxWaitMillis(60000L);
            pool = new JedisPool(conf, redisHost, redisPort, 100000);
        }
    }

    public static JedisPool getJedisPool() throws IOException {
        if (pool == null)
            makepool();
        return pool;
    }

    public static Jedis getJedis() throws IOException {
        RedisJedisPoolUtils redisJedisPoolUtils = new RedisJedisPoolUtils();
        if (redisJedisPoolUtils.jedis == null) {
            redisJedisPoolUtils.jedis = getJedisPool().getResource();
        }
        return redisJedisPoolUtils.jedis;
    }

    public static void cleanAll() {

        pool.close();
    }

    public static void cleanJedis(Jedis jedis) {
        if (pool != null) {

            pool.returnResource(jedis);
        }

    }
}

