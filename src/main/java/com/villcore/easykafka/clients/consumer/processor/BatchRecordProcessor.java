package com.villcore.easykafka.clients.consumer.processor;

import com.villcore.easykafka.clients.consumer.ConsumerRecord;
import com.villcore.easykafka.clients.consumer.CommitStatusEnum;

import java.util.List;

public interface BatchRecordProcessor<K, V> extends RecordProcessor<K, V> {
    public CommitStatusEnum onConsumerRecord(List<ConsumerRecord<K, V>> recordList);
}
