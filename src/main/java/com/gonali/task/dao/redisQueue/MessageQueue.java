package com.gonali.task.dao.redisQueue;


import com.gonali.task.dao.message.Message;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public interface MessageQueue {

    public MessageQueue setMessage(Message message);
    public Message pushMessage();
    public Message popMessage();
}
