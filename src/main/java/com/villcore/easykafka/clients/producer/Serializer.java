package com.villcore.easykafka.clients.producer;

import java.nio.charset.Charset;

/**
 * @Author: jiwei.guo
 * @Date: 2018/11/12 4:06 PM
 */
public interface Serializer {

    Charset UTF8 = Charset.forName("UTF-8");

    <T> byte[] serialize(T obj) throws Exception;

    <T> T deserialize(byte[] src, Class<T> clazz) throws Exception;

}
