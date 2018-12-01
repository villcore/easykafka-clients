package com.villcore.easykafka.clients.producer;


import com.villcore.easykafka.clients.SendCallback;
import com.villcore.easykafka.clients.SendResult;

import java.util.Map;
import java.util.concurrent.Future;

public interface EasyKafkaProducer<K, V> {

    SendResult sendSync(String topic, V value);

    com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, K key, V value);

    com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, int partition, K key, V value);

    com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, V value, Map<String, Object> header);

    com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, K key, V value, Map<String, Object> header);

    com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, int partition, K key, V value, Map<String, Object> header);

    Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, V value, SendCallback sendCallback);

    Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, K key, V value, com.villcore.easykafka.clients.producer.SendCallback sendCallback);

    Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, int partition, K key, V value, com.villcore.easykafka.clients.producer.SendCallback sendCallback);

    Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, V value, Map<String, Object> header, com.villcore.easykafka.clients.producer.SendCallback sendCallback);

    Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, K key, V value, Map<String, Object> header, com.villcore.easykafka.clients.producer.SendCallback sendCallback);

    Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, int partition, K key, V value, Map<String, Object> header, com.villcore.easykafka.clients.producer.SendCallback sendCallback);

    void flush();

    void close();

}
