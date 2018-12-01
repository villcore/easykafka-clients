package com.villcore.easykafka.clients.producer;


import java.util.Map;
import java.util.concurrent.Future;

public interface EasyKafkaProducer<K, V> {

    SendResult sendSync(String topic, V value);

    SendResult sendSync(String topic, K key, V value);

    SendResult sendSync(String topic, Integer partition, K key, V value);

    SendResult sendSync(String topic, V value, Map<String, Object> header);

    SendResult sendSync(String topic, K key, V value, Map<String, Object> header);

    SendResult sendSync(String topic, Integer partition, K key, V value, Map<String, Object> header);

    Future<SendResult> sendAsync(String topic, V value, SendCallback sendCallback);

    Future<SendResult> sendAsync(String topic, K key, V value, SendCallback sendCallback);

    Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, SendCallback sendCallback);

    Future<SendResult> sendAsync(String topic, V value, Map<String, Object> header, SendCallback sendCallback);

    Future<SendResult> sendAsync(String topic, K key, V value, Map<String, Object> header, SendCallback sendCallback);

    Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, Map<String, Object> header, SendCallback sendCallback);

    void flush();

    void close();

}
