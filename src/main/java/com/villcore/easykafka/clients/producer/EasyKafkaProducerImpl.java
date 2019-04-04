package com.villcore.easykafka.clients.producer;

import com.villcore.easykafka.clients.serialization.Serializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.network.Send;
import org.apache.kafka.common.record.AbstractRecords;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.record.RecordBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

@ThreadSafe
public class EasyKafkaProducerImpl<K, V> implements EasyKafkaProducer<K, V> {

    private static final Logger log = LoggerFactory.getLogger(EasyKafkaProducer.class);

    private AtomicBoolean isClosed = new AtomicBoolean(false);

    private KafkaProducer<byte[], byte[]> kafkaProducer;
    private Serializer<K> keySerializer;
    private Serializer<V> valueSerializer;

    public EasyKafkaProducerImpl(ProducerConfig producerConfig) {
        // TODO kafka config.
        // TODO replace serializer class.
        this.kafkaProducer = new KafkaProducer<>(producerConfig.toConfigMap());
    }

    @Override
    public void configSerializer(Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    @Override
    public SendResult sendSync(String topic, Integer partition, K key, V value, Map<String, byte[]> header) {
        ensureSerializerConfiged();
        try {
            return sendAsync(topic, partition, key, value, header, null).get();
        } catch (Exception e) {
            throw new RuntimeException("Send sync error", e);
        }
    }

    @Override
    public Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, Map<String, byte[]> header, SendCallback sendCallback) {
        ensureSerializerConfiged();
        ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<>(topic, partition, serializerKey(key), serializerValue(value), buildHeader(header));
        doSend(sendCallback, producerRecord);
        return null;
    }

    private void ensureSerializerConfiged() {
        Objects.requireNonNull(keySerializer, "Need config serilalizer before send");
        Objects.requireNonNull(keySerializer, "Need config serilalizer before send");
    }

    public byte[] serializerKey(K key) {
        return key == null ? null : keySerializer.serialize(key);
    }

    public byte[] serializerValue(V value) {
       return  value == null ? null : valueSerializer.serialize(value);
    }

    private RecordHeaders buildHeader(Map<String, byte[]> header) {
        RecordHeaders recordHeaders = new RecordHeaders();
        for(Map.Entry<String, byte[]> entry : header.entrySet()) {
            String headerKey = entry.getKey();
            byte[] headerValue = entry.getValue();
            recordHeaders.add(headerKey, headerValue);
        }
        return recordHeaders;
    }

    private Future<SendResult> doSend(SendCallback sendCallback, ProducerRecord<byte[], byte[]> producerRecord) {
        Future<RecordMetadata> sendFuture = kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    sendCallback.onException(exception);
                } else {
                    sendCallback.onAcknowledged(SendResult.fromMetadata(metadata));
                }
            }
        });
        return createSendResultFuture(sendFuture);
    }

    private Future<SendResult> createSendResultFuture(Future<RecordMetadata> sendFuture) {
        return null;
    }

    @Override
    public void flush() {
        if (!isClosed.get() && kafkaProducer != null) {
            kafkaProducer.flush();
        }
    }

    @Override
    public void close() {
        if (isClosed.compareAndSet(false, true) && kafkaProducer != null) {
            flush();
            kafkaProducer.close();
        }
    }

    private static class SendResultFuture implements Future<SendResult> {

        private Future<RecordMetadata> sendFuture;

        SendResultFuture (Future<RecordMetadata> sendFuture) {
            this.sendFuture = sendFuture;

        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public SendResult get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public SendResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    }
}
