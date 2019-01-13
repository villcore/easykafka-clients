package com.villcore.easykafka.clients.serialization;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;


public class JsonSerializer<T> implements Serializer<T> {

    private static final Gson GSON = new Gson();

    @Override
    public byte[] serialize(T object) {
        return GSON.toJson(object).getBytes(StandardCharsets.UTF_8);
    }
}
