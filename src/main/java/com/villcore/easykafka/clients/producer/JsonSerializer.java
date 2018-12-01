package com.villcore.easykafka.clients.producer;

import com.villcore.easykafka.clients.Serializer;
import org.apache.kafka.common.utils.Utils;

public class JsonSerializer<T> implements Serializer<T> {

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] src, Class<T> clazz) throws Exception {
        return null;
    }

    public static void main(String[] args) throws Exception {
        Serializer<String> serializer = Utils.newInstance(JsonSerializer.class.getName(), Serializer.class);

        System.out.println(serializer.serialize("a"));
    }
}
