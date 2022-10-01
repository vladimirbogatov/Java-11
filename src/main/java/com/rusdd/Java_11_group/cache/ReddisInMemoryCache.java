package com.rusdd.Java_11_group.cache;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReddisInMemoryCache {
    String get(String key);

    String set(String key, String value);

    int del(String... keys);

    List<String> keys(String pattern);

    long expire(String key, long time);

    long ttl(String key);
}
