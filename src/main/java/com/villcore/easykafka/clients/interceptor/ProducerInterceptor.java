package com.villcore.easykafka.clients.interceptor;

import com.villcore.easykafka.clients.producer.SendResult;

import java.util.Map;

public interface ProducerInterceptor<K, V> {

    public void onSend(String topic, Integer partition, K key, V value, Map<String, byte[]> headers);

    public void onAcknowledge(SendResult sendResult, Exception e);
}
