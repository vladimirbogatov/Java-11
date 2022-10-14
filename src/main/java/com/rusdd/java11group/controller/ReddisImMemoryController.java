package com.rusdd.java11group.controller;

import com.rusdd.java11group.cache.ReddisInMemoryCache;
import com.rusdd.java11group.service.ReddisInMemoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ReddisImMemoryController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ReddisImMemoryController {
    protected static final String REST_URL = "/api/cache";

    @Qualifier("SRCache")
    @Autowired
    private ReddisInMemoryCache cache;
    @Autowired
    private ReddisInMemoryService service;

    @GetMapping("/{key}")
    @Operation(summary = "get value by key. Return value")
    public String get(@PathVariable String key) {
        log.info("get by key {}", key);
        return cache.get(key);
    }

    @PostMapping("/{key}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type")
    public String set(@PathVariable String key, @RequestParam String value) {
        log.info("Set key={} to hold the value={}", key, value);
        return cache.set(key, value);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Removes the specified keys. A key is ignored if it does not exist")
    public int del(@RequestBody List<String> keys) {
        log.info("Removes the keys {}", keys);
        return service.del(keys);
    }

    @GetMapping("/keys")
    @Operation(summary = "Returns all keys matching pattern")
    public List<String> keys(@RequestParam String pattern) {
        log.info("Returns all keys matching pattern = {}", pattern);
        return cache.keys(pattern);
    }

    @PostMapping("/expire/{key}")
    @Operation(summary = "Set a timeout (sec) on key")
    public Long expire(@PathVariable String key, @RequestParam Long sec) {
        log.info("Set a timeout={} (sec) on key={}", sec, key);
        return cache.expire(key, sec);
    }

    @GetMapping("/ttl/{key}")
    @Operation(summary = "Returns the remaining time (sec) to live of a key that has a timeout")
    public Long ttl(@PathVariable String key) {
        log.info("Return the remaining time to live for key={}", key);
        return cache.ttl(key);
    }
}
