package com.villcore.easykafka.clients.producer;


public interface SendCallback {

    void onException(Exception exception);

    void onAcknowledged(SendResult result);
}
