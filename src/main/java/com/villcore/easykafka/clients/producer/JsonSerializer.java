package com.villcore.easykafka.clients.producer;

import com.google.gson.Gson;

public class JsonSerializer<T> implements Serializer<T> {

    private static final String CHARSET = "utf-8";

    private static final Gson GSON = new Gson();

    @Override
    public byte[] serialize(T obj) throws Exception {
        return GSON.toJson(obj).getBytes(CHARSET);
    }

    @Override
    public T deserialize(byte[] src, Class<T> clazz) throws Exception {
        return GSON.fromJson(new String(src, CHARSET), clazz);
    }
}
