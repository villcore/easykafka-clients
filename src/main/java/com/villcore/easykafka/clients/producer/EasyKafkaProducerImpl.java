package com.villcore.easykafka.clients.producer;

import com.villcore.easykafka.clients.SendCallback;
import com.villcore.easykafka.clients.SendResult;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

@ThreadSafe
public class EasyKafkaProducerImpl<K, V> implements com.villcore.easykafka.clients.producer.EasyKafkaProducer<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyKafkaProducerImpl.class);

    private final KafkaProducer<byte[], byte[]> producer;

    public EasyKafkaProducerImpl(Properties prop) {
        producer = new KafkaProducer<byte[], byte[]>(prop);
    }


    @Override
    public SendResult sendSync(String topic, V value) {
        return null;
    }

    @Override
    public com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, K key, V value) {
        return null;
    }

    @Override
    public com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, int partition, K key, V value) {
        return null;
    }

    @Override
    public com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, V value, Map<String, Object> header) {
        return null;
    }

    @Override
    public com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, K key, V value, Map<String, Object> header) {
        return null;
    }

    @Override
    public com.villcore.easykafka.clients.producer.SendResult sendSync(String topic, int partition, K key, V value, Map<String, Object> header) {
        return null;
    }

    @Override
    public Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, V value, SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, K key, V value, com.villcore.easykafka.clients.producer.SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, int partition, K key, V value, com.villcore.easykafka.clients.producer.SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, V value, Map<String, Object> header, com.villcore.easykafka.clients.producer.SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, K key, V value, Map<String, Object> header, com.villcore.easykafka.clients.producer.SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<com.villcore.easykafka.clients.producer.SendResult> sendAsync(String topic, int partition, K key, V value, Map<String, Object> header, com.villcore.easykafka.clients.producer.SendCallback sendCallback) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {

    }
}
