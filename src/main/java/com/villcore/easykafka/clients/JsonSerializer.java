package com.villcore.easykafka.clients.serializer;

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
        com.villcore.easykafka.clients.serializer.Serializer<String> serializer = Utils.newInstance(JsonSerializer.class.getName(), com.villcore.easykafka.clients.serializer.Serializer.class);

        System.out.println(serializer.serialize("a"));
    }
}
