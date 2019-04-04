package com.villcore.easykafka.clients.serialization;

public interface Serializer<T> {

    public byte[] serialize(T object);

}
