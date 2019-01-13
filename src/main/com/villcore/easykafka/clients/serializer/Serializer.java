package com.villcore.easykafka.clients.serializer;

public interface Serializer<T> {

    public byte[] serialize(T obj);
}
