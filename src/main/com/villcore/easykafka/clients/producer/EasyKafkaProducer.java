package com.villcore.easykafka.clients.producer;

import java.util.Map;
import java.util.concurrent.Future;

public interface EasyKafkaProducer<K, V> {

    SendResult sendSync(String topic, Integer partition, K key, V value, Map<String, byte[]> header);

    Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, Map<String, byte[]> header, SendCallback sendCallback);

    void flush();

    void close();

}
