package com.villcore.easykafka.clients.producer;

import java.nio.charset.Charset;

public interface Serializer<T> {

    Charset UTF8 = Charset.forName("UTF-8");

    byte[] serialize(T obj) throws Exception;

    T deserialize(byte[] src, Class<T> clazz) throws Exception;

}
