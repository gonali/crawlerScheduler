package com.gonali.task.redisQueue;


import com.gonali.task.model.TaskModel;
import com.gonali.task.utils.ObjectSerializeUtils;
import com.gonali.task.utils.RedisJedisPoolUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskQueue {


    private static final String QUEUE_KEY = QueueKeys.QUEUE_KEY_TASK;
    private static final int dbIndex = 7;

    private TaskQueue() {

    }


    public static void pushCrawlerTaskQueue(TaskModel taskModel) {

        Jedis jedis = null;

        try {

            byte[] bytes = ObjectSerializeUtils.serializeToBytes(taskModel);
            jedis = RedisJedisPoolUtils.getJedis();
            jedis.select(dbIndex);
            jedis.lpush(QUEUE_KEY.getBytes(), bytes);

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            RedisJedisPoolUtils.cleanJedis(jedis);
        }

    }

    public static TaskModel popCrawlerTaskQueue() {

        Jedis jedis = null;

        try {

            jedis = RedisJedisPoolUtils.getJedis();
            jedis.select(dbIndex);
            byte[] bytes = jedis.rpop(QUEUE_KEY.getBytes());
            if (bytes == null)
                return null;
            TaskModel taskModel = (TaskModel) ObjectSerializeUtils.getEntityFromBytes(bytes);

            return taskModel;

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            RedisJedisPoolUtils.cleanJedis(jedis);
        }

        return null;
    }

    public static long queueLenth() {

        Jedis jedis = null;

        try {
            jedis = RedisJedisPoolUtils.getJedis();
            jedis.select(dbIndex);
            return jedis.llen(QUEUE_KEY.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            RedisJedisPoolUtils.cleanJedis(jedis);
        }

        return 0;
    }

}
