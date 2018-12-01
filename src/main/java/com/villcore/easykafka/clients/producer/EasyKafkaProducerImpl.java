package com.villcore.easykafka.clients.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

@ThreadSafe
public class EasyKafkaProducerImpl<K, V> implements EasyKafkaProducer<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyKafkaProducerImpl.class);

    private Serializer serializer;

    private final KafkaProducer<byte[], byte[]> producer;

    private volatile boolean closed;

    public EasyKafkaProducerImpl(Properties prop) {
        String serializerClazzName = prop.getProperty("serializer.class", JsonSerializer.class.getName());
        this.serializer = initSerializer(serializerClazzName);
        this.producer = new KafkaProducer<>(prop);

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    private Serializer initSerializer(String serializerClazzName) {
        try {
            return Utils.newInstance(serializerClazzName, Serializer.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SendResult sendSync(String topic, V value) {
        return sendSync(topic, null, value);
    }

    @Override
    public SendResult sendSync(String topic, K key, V value) {
        return sendSync(topic, null, key, value);
    }

    @Override
    public SendResult sendSync(String topic, Integer partition, K key, V value) {
        return doSend(topic, null, key, value, Collections.emptyMap(), null).get();
    }

    @Override
    public SendResult sendSync(String topic, V value, Map<String, Object> header) {
        return null;
    }

    @Override
    public SendResult sendSync(String topic, K key, V value, Map<String, Object> header) {
        return null;
    }

    @Override
    public SendResult sendSync(String topic, Integer partition, K key, V value, Map<String, Object> header) {
        return null;
    }

    @Override
    public Future<SendResult> sendAsync(String topic, V value, SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<SendResult> sendAsync(String topic, K key, V value, SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<SendResult> sendAsync(String topic, V value, Map<String, Object> header, SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<SendResult> sendAsync(String topic, K key, V value, Map<String, Object> header, SendCallback sendCallback) {
        return null;
    }

    @Override
    public Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, Map<String, Object> header, SendCallback sendCallback) {
        return null;
    }

    private final Future<SendResult> doSend(String topic, Integer partition, K key, V value, Map<String, Object> header, SendCallback sendCallback) {
        if (!closed) {
            try {
                byte[] keyBytes = serializer.serialize(key);
                byte[] valueBytes = serializer.serialize(value);
                // TODO extract method.
                RecordHeaders recordHeaders = new RecordHeaders();
                if (!header.isEmpty()) {
                    for (Map.Entry<String, Object> entry : header.entrySet()) {
                        String headerKey = entry.getKey();
                        Object headerValue = entry.getValue();
                        byte[] headerValueBytes = serializer.serialize(headerValue);
                        recordHeaders.add(headerKey, headerValueBytes);
                    }
                }
                ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, partition, serializer.serialize(key), serializer.serialize(value), recordHeaders);

            } catch (Exception e) {
                throw new IllegalStateException("EasyKafkaProducerImpl send record error.", e);
            }
        }
        throw new IllegalStateException("EasyKafkaProducerImpl has been closed.");
    }

    private final Map<String, Object> immutableMap(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return Collections.emptyMap();
        } else {
            return Collections.unmodifiableMap(new HashMap<>(map));
        }
    }

    @Override
    public void flush() {
        if (!closed) {
            producer.flush();
        }
    }

    @Override
    public void close() {
        if (!closed) {
            flush();
            producer.close();
        }
    }
}
