package com.villcore.easykafka.clients.serialization;

public interface Deserializer<T> {

    public T deserialize(byte[] data, Class<T> clazz);

}
