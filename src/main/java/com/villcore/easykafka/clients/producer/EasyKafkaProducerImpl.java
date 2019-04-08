package com.villcore.easykafka.clients.producer;

import com.villcore.easykafka.clients.interceptor.ProducerInterceptor;
import com.villcore.easykafka.clients.serialization.Serializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    private ProducerInterceptor<K, V> interceptor;

    public EasyKafkaProducerImpl(ProducerConfig<K, V> producerConfig) {
        this.keySerializer = producerConfig.getKeySerializer();
        this.valueSerializer = producerConfig.getValueSerializer();
        this.interceptor = producerConfig.getInterceptor();
        ensureSerializerConfiged();

        Map<String, Object> configs = new HashMap<>(producerConfig.getConfigs());
        configs.put("bootstrap.server", producerConfig.getBoostrapServer());
        configs.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.kafkaProducer = new KafkaProducer<>(configs);
    }

    private void ensureSerializerConfiged() {
        Objects.requireNonNull(keySerializer, "Need config serilalizer");
        Objects.requireNonNull(keySerializer, "Need config serilalizer");
    }

    @Override
    public SendResult sendSync(String topic, Integer partition, K key, V value, Map<String, byte[]> header) {
        try {
            return sendAsync(topic, partition, key, value, header, null).get();
        } catch (Exception e) {
            throw new RuntimeException("Send sync error", e);
        }
    }

    @Override
    public Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, Map<String, byte[]> header, SendCallback sendCallback) {
        interceptor.onSend(topic, partition, key, value, header);
        ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<>(topic, partition,
                serializerKey(key), serializerValue(value), buildHeader(header));
        return doSend(producerRecord, sendCallback);
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

    private Future<SendResult> doSend(ProducerRecord<byte[], byte[]> producerRecord, SendCallback sendCallback) {
        Future<RecordMetadata> sendFuture = kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                SendResult sendResult = SendResult.fromMetadata(metadata);
                interceptor.onAcknowledge(sendResult, exception);
                if (exception != null) {
                    sendCallback.onException(exception);
                } else {
                    sendCallback.onAcknowledged(sendResult);
                }
            }
        });
        return createSendResultFuture(sendFuture);
    }

    private Future<SendResult> createSendResultFuture(Future<RecordMetadata> sendFuture) {
        return new SendResultFuture(sendFuture);
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

    /**
     * SendResultFuture
     */
    private static class SendResultFuture implements Future<SendResult> {

        private Future<RecordMetadata> sendFuture;

        SendResultFuture (Future<RecordMetadata> sendFuture) {
            this.sendFuture = sendFuture;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return sendFuture.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return sendFuture.isCancelled();
        }

        @Override
        public boolean isDone() {
            return sendFuture.isDone();
        }

        @Override
        public SendResult get() throws InterruptedException, ExecutionException {
            RecordMetadata recordMetadata = sendFuture.get();
            return SendResult.fromMetadata(recordMetadata);
        }

        @Override
        public SendResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            RecordMetadata recordMetadata = sendFuture.get(timeout, unit);
            return SendResult.fromMetadata(recordMetadata);
        }
    }
}
