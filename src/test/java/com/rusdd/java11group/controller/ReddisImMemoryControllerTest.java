package com.rusdd.java11group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rusdd.java11group.AbstractControllerTest;
import com.rusdd.java11group.cache.ReddisInMemoryCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReddisImMemoryControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ReddisImMemoryController.REST_URL + "/";
    @Qualifier("SRCache")
    @Autowired
    ReddisInMemoryCache cache;
    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void del() throws Exception {
        cache.set("key_del", "value_del");
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList("key_del"))))
                .andExpect(status().isNoContent())
                .andDo(print());

    }
}
