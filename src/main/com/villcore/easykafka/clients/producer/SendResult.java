package com.villcore.easykafka.clients.producer;

import java.io.Serializable;
import java.util.Objects;


public class SendResult implements Serializable {

    public static final int UNKNOWN_PARTITION = -1;

    private final String topic;
    private final int partition;
    private final long offset;
    private final long timestamp;
    private final int serializedKeySize;
    private final int serializedValueSize;
    private volatile Long checksum;

    public SendResult(String topic, int partition, long offset, long timestamp, int serializedKeySize, int serializedValueSize, Long checksum) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
        this.serializedKeySize = serializedKeySize;
        this.serializedValueSize = serializedValueSize;
        this.checksum = checksum;
    }

    public String getTopic() {
        return topic;
    }

    public int getPartition() {
        return partition;
    }

    public long getOffset() {
        return offset;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getSerializedKeySize() {
        return serializedKeySize;
    }

    public int getSerializedValueSize() {
        return serializedValueSize;
    }

    public Long getChecksum() {
        return checksum;
    }

    public void setChecksum(Long checksum) {
        this.checksum = checksum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendResult that = (SendResult) o;
        return partition == that.partition &&
                offset == that.offset &&
                timestamp == that.timestamp &&
                serializedKeySize == that.serializedKeySize &&
                serializedValueSize == that.serializedValueSize &&
                topic.equals(that.topic) &&
                checksum.equals(that.checksum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, partition, offset, timestamp, serializedKeySize, serializedValueSize, checksum);
    }

    @Override
    public String toString() {
        return "SendResult{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                ", timestamp=" + timestamp +
                ", serializedKeySize=" + serializedKeySize +
                ", serializedValueSize=" + serializedValueSize +
                ", checksum=" + checksum +
                '}';
    }
}
