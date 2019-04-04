package com.villcore.easykafka.clients.common;

import java.util.Objects;

public class TopicPartition {

    private final String topic;
    private final Integer partition;

    public TopicPartition(String topic, Integer partition) {
        this.topic = topic;
        this.partition = partition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicPartition that = (TopicPartition) o;
        return Objects.equals(topic, that.topic) &&
                Objects.equals(partition, that.partition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, partition);
    }

    @Override
    public String toString() {
        return "TopicPartition{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                '}';
    }
}
