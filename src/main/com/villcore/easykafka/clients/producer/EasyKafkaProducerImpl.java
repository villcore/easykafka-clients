package com.villcore.easykafka.clients.producer;

import com.villcore.easykafka.clients.serialization.Serializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeaders;
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

    private static final Logger log = LoggerFactory.getLogger(EasyKafkaProducerImpl.class);

    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final boolean enableShutdownHook;

    private final KafkaProducer<byte[], byte[]> producer;
    private final Serializer<K> keySerialize;
    private final Serializer<V> valueSerializer;

    public EasyKafkaProducerImpl(Properties prop) {
        // TODO config producer.

        this.keySerialize = null;
        this.valueSerializer = null;
        this.producer = new KafkaProducer<>(prop);

        this.enableShutdownHook = false;
        if (enableShutdownHook) {
            Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        }
    }

    @Override
    public SendResult sendSync(String topic, Integer partition, K key, V value, Map<String, byte[]> header) {
        try {
            return doSend(topic, partition, key, value, header, null).get();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Future<SendResult> sendAsync(String topic, Integer partition, K key, V value, Map<String, byte[]> header, SendCallback sendCallback) {
        try {
            return doSend(topic, partition, key, value, header, sendCallback);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private final Future<SendResult> doSend(String topic, Integer partition, K key, V value, Map<String, byte[]> header, SendCallback sendCallback) {
        try {
            byte[] keyBytes = keySerialize.serialize(key);
            byte[] valueBytes = valueSerializer.serialize(value);
            RecordHeaders recordHeaders = new RecordHeaders();
            if (header != null && !header.isEmpty()) {
                for (Map.Entry<String, byte[]> entry : header.entrySet()) {
                    String headerKey = entry.getKey();
                    byte[] headerValue = entry.getValue();
                    recordHeaders.add(headerKey, headerValue);
                }
            }
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, partition, keyBytes, valueBytes, recordHeaders);
            // TODO may be reject.
            ProducerRejectPolicy policy = null;
            int recordSizeInBytes = AbstractRecords.estimateSizeInBytesUpperBound(RecordBatch.MAGIC_VALUE_V2,
                    CompressionType.NONE, keyBytes, valueBytes, recordHeaders.toArray());
            if (!isClosed.get()) {
                Future<RecordMetadata> sendFuture = producer.send(record, sendCallback != null ? new ProducerCallback(sendCallback) : null);
                return new SendResultFuture(sendFuture);
            } else {
                throw new IllegalStateException("EasyKafkaProducerImpl has been closed.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("EasyKafkaProducerImpl send record error.", e);
        }
    }

    @Override
    public void flush() {
        if (!isClosed.get()) {
            producer.flush();
        }
    }

    @Override
    public void close() {
        if (isClosed.compareAndSet(false, true)) {
            log.info("EasyKafkaProducerImpl closing.");
            flush();
            producer.close();
            log.info("EasyKafkaProducerImpl closed.");
        }
    }

    private SendResult createSendResult(RecordMetadata recordMetadata) {
        return new SendResult(recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
                recordMetadata.timestamp(), recordMetadata.serializedKeySize(), recordMetadata.serializedValueSize(),
                recordMetadata.checksum());
    }

    /**
     *  Producer callback
     */
    private class ProducerCallback implements Callback {

        private SendCallback wrappedCallback;

        public ProducerCallback(SendCallback wrappedCallback) {
            this.wrappedCallback = wrappedCallback;
        }

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (wrappedCallback == null) {
                return;
            }

            if (exception != null) {
                wrappedCallback.onException(exception);
            } else {
                wrappedCallback.onAcknowledged(createSendResult(metadata));
            }
        }
    }

    /**
     * Send future wrap KafkaProducer send future
     */
    private class SendResultFuture implements Future<SendResult> {

        private Future<RecordMetadata> wrappedRecordMetadata;

        public SendResultFuture(Future<RecordMetadata> wrappedRecordMetadata) {
            Objects.requireNonNull(wrappedRecordMetadata);
            this.wrappedRecordMetadata = wrappedRecordMetadata;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return wrappedRecordMetadata.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return wrappedRecordMetadata.isCancelled();
        }

        @Override
        public boolean isDone() {
            return wrappedRecordMetadata.isDone();
        }

        @Override
        public SendResult get() throws InterruptedException, ExecutionException {
            return createSendResult(wrappedRecordMetadata.get());
        }

        @Override
        public SendResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return createSendResult(wrappedRecordMetadata.get(timeout, unit));
        }
    }
}
