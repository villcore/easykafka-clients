package com.villcore.easykafka.clients.serialization;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public class JsonDeserializer<T> implements Deserializer <T> {

    private static final Gson GSON = new Gson();

    @Override
    public T deserialize(byte[] data, Class<T> clazz) {
        try {
            return GSON.fromJson(new String(data, StandardCharsets.UTF_8), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Deserialize data error.", e);
        }
    }
}
