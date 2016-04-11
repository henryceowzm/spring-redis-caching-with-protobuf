package com.henry.spring.redis.config;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

  private static final Logger log = Logger.getLogger(CacheConfig.class);

  @Bean
  public JedisConnectionFactory redisConnectionFactory() {
    JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
    redisConnectionFactory.setHostName("127.0.0.1");
    redisConnectionFactory.setPort(6379);
    return redisConnectionFactory;
  }

  @Bean
  public RedisTemplate redisTemplate(RedisConnectionFactory cf) {
    RedisTemplate redisTemplate = new RedisTemplate();
    redisTemplate.setEnableDefaultSerializer(false);
    redisTemplate.setKeySerializer(keySerializer());
    redisTemplate.setHashKeySerializer(keySerializer());
    redisTemplate.setValueSerializer(valueSerializer());
    redisTemplate.setHashValueSerializer(valueSerializer());
    redisTemplate.setConnectionFactory(cf);
    return redisTemplate;
  }
  
  @Bean
  public RedisSerializer keySerializer(){
      return new GenericToStringSerializer(String.class);
  }
  
  @Bean
  public RedisSerializer valueSerializer(){
      return new Jackson2JsonRedisSerializer<Object>(Object.class);
  }

  @Bean
  public CacheManager cacheManager(RedisTemplate redisTemplate) {
    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
    cacheManager.setDefaultExpiration(300);
    return cacheManager;
  }

  @Bean
  public KeyGenerator customKeyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object o, Method method, Object... objects) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName());
        sb.append(method.getName());
        for (Object obj : objects) {
          sb.append(obj.toString());
        }
        log.debug("customKeyGenerator: " + sb);
        return sb.toString();
      }
    };
  }

}
