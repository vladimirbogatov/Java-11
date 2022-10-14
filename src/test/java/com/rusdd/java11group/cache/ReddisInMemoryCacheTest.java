package com.rusdd.java11group.cache;

import com.rusdd.java11group.AbstractControllerTest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.TimeUnit;

class ReddisInMemoryCacheTest extends AbstractControllerTest {

    @Qualifier("SRCache")
    @Autowired
    ReddisInMemoryCache cache;


    @Test
    void expire() {
        cache.set("test", "test1");
        cache.expire("test", 1);
        try {
            TimeUnit.MILLISECONDS.sleep(1700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNull(cache.get("test"));

    }


    @Test
    void set() {
        cache.set("test", "test1");
        Assert.assertEquals("test1", cache.get("test"));
        cache.del("test");
    }
}