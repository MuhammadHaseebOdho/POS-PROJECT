package com.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToken(String token, String username, long durationInSeconds) {
        // Save token with expiry time
        redisTemplate.opsForValue().set(token, username, durationInSeconds, TimeUnit.SECONDS);
    }

    public boolean validateToken(String token) {
        // Check if the token exists in Redis
        System.out.println("TOKEN:::"+token);
        return redisTemplate.hasKey(token);
    }

    public void deleteToken(String token) {
        // Delete token from Redis
        redisTemplate.delete(token);
    }


}
