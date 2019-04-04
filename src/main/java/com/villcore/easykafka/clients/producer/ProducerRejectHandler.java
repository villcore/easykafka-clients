package com.villcore.easykafka.clients.producer;

import java.util.Map;

public interface ProducerRejectHandler {
    public void rejectProducerRecord(String topic, Integer partition, K key, V value, Map<String, byte[]> header, SendCallback sendCallback);
}
