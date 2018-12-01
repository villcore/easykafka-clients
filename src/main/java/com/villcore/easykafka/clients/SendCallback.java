package com.villcore.easykafka.clients.producer;

import com.villcore.easykafka.clients.SendResult;

/**
 * @Author: jiwei.guo
 * @Date: 2018/11/13 11:09 AM
 */
public interface SendCallback {

    public void onException(Exception exception);

    public void onAcknowledged(SendResult result);

}
