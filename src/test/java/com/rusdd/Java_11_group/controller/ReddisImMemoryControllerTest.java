package com.rusdd.Java_11_group.controller;

import com.rusdd.Java_11_group.AbstractControllerTest;
import com.rusdd.Java_11_group.cache.ReddisInMemoryCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReddisImMemoryControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ReddisImMemoryController.REST_URL + "/";
    @Qualifier("SRCache")
    @Autowired
    ReddisInMemoryCache cache;

    @Test
    void get() throws Exception {
        cache.set("key1", "string1");
        perform(MockMvcRequestBuilders.get(REST_URL + "key1"))
                .andExpect(status().isOk())
                .andExpect(content().string("string1"));
    }

    @Test
    void set() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "key1")
                .param("value", "string1"))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}