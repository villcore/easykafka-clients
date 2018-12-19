package com.villcore.easykafka.clients.serializer;

import com.google.gson.Gson;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonSerializer<T> implements Serializer<T>, Deserializer<T> {

    private static final String CHARSET = "utf-8";
    private static final Gson GSON = new Gson();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return GSON.toJson(data).getBytes(CHARSET);
        } catch (Exception e) {
            throw new SerializationException();
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return GSON.fromJson(new String(data, CHARSET), (Type) Object.class);
        } catch (Exception e) {
            throw new SerializationException();
        }
    }

    @Override
    public void close() {
    }
}
