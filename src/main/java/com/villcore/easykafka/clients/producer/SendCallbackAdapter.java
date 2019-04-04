package com.villcore.easykafka.clients.producer;

public class SendCallbackAdapter<K, V> implements SendCallback {

    private String topic;
    private K key;
    private V value;

    public SendCallbackAdapter(String topic, K key, V value) {
        this.topic = topic;
        this.key = key;
        this.value = value;
    }

    @Override
    public void onException(Exception exception) {
        onException(exception, key, value);
    }

    @Override
    public void onAcknowledged(SendResult result) {
        onAcknowledged(result, key, value);
    }

    public void onException(Exception exception, K key, V value) {

    }

    public void onAcknowledged(SendResult sendResult, K key, V value) {

    }
}
