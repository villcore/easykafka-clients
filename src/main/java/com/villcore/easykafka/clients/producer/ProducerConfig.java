package com.villcore.easykafka.clients.producer;

import com.villcore.easykafka.clients.interceptor.ProducerInterceptor;
import com.villcore.easykafka.clients.serialization.Serializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProducerConfig<K, V> {

    private final String boostrapServer;
    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;
    private final ProducerInterceptor<K, V> interceptor;
    private final Map<String, Object> configs;

    private ProducerConfig(Builder builder) {
        this.boostrapServer = builder.boostrapServer;
        this.keySerializer = builder.keySerializer;
        this.valueSerializer = builder.valueSerializer;
        this.interceptor = builder.interceptor;
        this.configs = Collections.unmodifiableMap(builder.configs);
    }

    public String getBoostrapServer() {
        return boostrapServer;
    }

    public Serializer<K> getKeySerializer() {
        return keySerializer;
    }

    public Serializer<V> getValueSerializer() {
        return valueSerializer;
    }

    public ProducerInterceptor<K, V> getInterceptor() {
        return interceptor;
    }

    public Map<String, Object> getConfigs() {
        return configs;
    }

    public class Builder {

        private String boostrapServer;
        private Serializer<K> keySerializer;
        private Serializer<V> valueSerializer;
        private ProducerInterceptor<K, V> interceptor;
        private Map<String, Object> configs;

        public Builder() {
            this.configs = new HashMap<>();
        }

        public Builder boostrapServer(String boostrapServer) {
            this.boostrapServer = boostrapServer;
            return this;
        }

        public Builder keySerializer(Serializer<K> keySerializer) {
            this.keySerializer = keySerializer;
            return this;
        }

        public Builder valueSerializer(Serializer<V> valueSerializer) {
            this.valueSerializer = valueSerializer;
            return this;
        }

        public Builder producerInterceptor(ProducerInterceptor<K, V> interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public Builder config(String key, Object value) {
            this.configs.put(key, value);
            return this;
        }

        public ProducerConfig build() {
            return new ProducerConfig(this);
        }
    }
}
