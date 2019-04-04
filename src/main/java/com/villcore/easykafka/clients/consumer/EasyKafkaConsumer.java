package com.villcore.easykafka.clients.consumer;

import com.villcore.easykafka.clients.consumer.processor.RecordProcessor;
import org.apache.kafka.common.TopicPartition;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;

@ThreadSafe
public interface EasyKafkaConsumer<K, V> {
    /**
     * Consumer 提供两种类型的使用方式
     *
     * 1. Consumer包含一个拉取线程，自动触发执行
     * 2. Consumer依赖主线程，如果主线程fire poll，执行
     *
     *
     *
     */
    public void registerRecordProcesser(RecordProcessor);

    public void commitSync();

    public void commitSync(Map<TopicPartition, Long> offset);

    public void commitAsync();

    public void commitAsync(Map<TopicPartition, Long> offset);

    public void close();

}
