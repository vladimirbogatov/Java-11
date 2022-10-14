package com.rusdd.java11group.service;

import com.rusdd.java11group.cache.ReddisInMemoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReddisInMemoryService {

    @Qualifier("SRCache")
    @Autowired
    private ReddisInMemoryCache cache;

    public int del(List<String> keys) {
        String[] arrayKeys = new String[keys.size()];
        return cache.del(keys.toArray(arrayKeys));
    }


}
