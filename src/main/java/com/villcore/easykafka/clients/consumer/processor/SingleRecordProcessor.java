package com.villcore.easykafka.clients.consumer.processor;

import com.villcore.easykafka.clients.consumer.ConsumerRecord;
import com.villcore.easykafka.clients.consumer.CommitStatusEnum;

public interface SingleRecordProcessor<K, V> extends RecordProcessor<K, V> {
    public CommitStatusEnum onConsumerRecord(ConsumerRecord<K, V> record);
}
