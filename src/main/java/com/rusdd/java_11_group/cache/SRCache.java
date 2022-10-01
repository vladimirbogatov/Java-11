package com.rusdd.java_11_group.cache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AllArgsConstructor

@Component
@Slf4j
public class SRCache implements ReddisInMemoryCache {

    private static final long serialVersionUID = 1L;
    private Map<String, SoftReference<String>> map = new ConcurrentHashMap<>();
    private Map<String, Long> timeMap = new ConcurrentHashMap<>();
    private boolean mapAlive = true;
    private long ttlInMillisecond;

    public SRCache(long ttlInMillisecond) {
        this.ttlInMillisecond = ttlInMillisecond;
        initialize();
    }

    public SRCache() {
        this.ttlInMillisecond = 10000;
        initialize();
    }

    void initialize() {
        new CleanerThread().start();
    }

    @Override
    public String get(String key) {
        SoftReference<String> softReferenceValue = map.get(key);
        return softReferenceValue != null ? softReferenceValue.get() : null;
    }

    @Override
    public String set(String key, String value) {
        SoftReference<String> softReference = map.put(key, new SoftReference<>(value));
        timeMap.put(key, new Date().getTime() + ttlInMillisecond);
        if (softReference != null) {
            String result = softReference.get();
            softReference.clear();
            return result;
        } else {
            return null;
        }
    }

    @Override
    public int del(String... keys) {
        int result = 0;
        for (String key : keys
        ) {
            SoftReference<String> softReference = map.remove(key);
            timeMap.remove(key);
            if (softReference.get() != null) {
                result++;
                softReference.clear();
            }
        }
        return result;
    }

    @Override
    public List<String> keys(String strPattern) {
        Set<String> kSet = map.keySet();
        return kSet.stream().filter(k -> Pattern.matches(strPattern, k))
                .collect(Collectors.toList());
    }

    /**
     * @param key  key
     * @param time Set a timeout on key in seconds
     * @return just settled timeout
     */
    @Override
    public long expire(String key, long time) {
        timeMap.put(key, new Date().getTime() + time * 1000);
        return time;
    }

    @Override
    public long ttl(String key) {
        return (timeMap.get(key) - new Date().getTime()) / 1000;
    }

    private class CleanerThread extends Thread {
        @Override
        public void run() {
            log.info("start clean");
            while (mapAlive) {
                cleanMap();
                try {
                    Thread.sleep(ttlInMillisecond / 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cleanMap() {
            long currentTime = new Date().getTime();
            for (String key : timeMap.keySet()) {
                if (currentTime > (timeMap.get(key))) {
                    del(key);
                    timeMap.remove(key);
                    log.info("key={} was removed", key);
                }

            }
        }
    }
}
