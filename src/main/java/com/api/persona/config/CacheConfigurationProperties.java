package com.api.persona.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "cache")
public class CacheConfigurationProperties {

    public static final String PERSONAS_CACHE = "personas-cache";

    private long ttlDefault = 60; // For dynamically created RedisCaches.
    private int redisPort = 6379;
    private String redisHost = "localhost";

    // Mapping of cacheNames to expira-after-write timeout in seconds
    private Map<String, Long> cacheExpirations = new HashMap<>();

    public long getTtlDefault() {
        return ttlDefault;
    }

    public void setTtlDefault(long ttlDefault) {
        this.ttlDefault = ttlDefault;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Map<String, Long> getCacheExpirations() {
        return cacheExpirations;
    }

    public void setCacheExpirations(Map<String, Long> cacheExpirations) {
        this.cacheExpirations = cacheExpirations;
    }
}
