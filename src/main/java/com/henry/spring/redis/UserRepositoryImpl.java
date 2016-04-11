package com.henry.spring.redis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.henry.spring.redis.User.UserGroup;
import com.henry.spring.redis.serializer.ProtobufSerializer;

@Component
public class UserRepositoryImpl implements UserRepository {

    private static final Logger log = Logger.getLogger(UserRepositoryImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Charset charSet = Charset.forName("utf-8");
    private final ProtobufSerializer<UserProtos.User> serializer = new ProtobufSerializer<UserProtos.User>(UserProtos.User.class);
    private final Jackson2JsonRedisSerializer<User> jackson2JsonRedisSerializer =  new Jackson2JsonRedisSerializer<User>(User.class);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // private ValueOperations<String, User> operations;
    //
    @PostConstruct
    public void init() {
        
        // this.operations=this.redisTemplate.opsForValue();
    }

    @Override
    public User saveUser(Long id) {
        log.debug("Saving user...");
        User user = new User();
        user.setId(id);
        return user;
    }

    @Override
    public User getUser(Long id) {
        log.debug("Retrieving user...");
        User user = new User();
        user.setId(id);
        user.setUserName("中文测试");
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Deleting user...");
        // delete user
    }

    @Override
    public User getFromRedis(final Long id) {
        log.debug("Getting user from redis...");
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        return (User)redisTemplate.opsForValue().get(id);
        // return redisTemplate.execute(new RedisCallback<User>() {
        //
        // @Override
        // public User doInRedis(RedisConnection connection) throws DataAccessException {
        // byte[] bytes= connection.get(id.toString().getBytes(charSet));
        // log.debug("doInRedis: "+new String(bytes,charSet));
        // User user = null;
        // try {
        // user = objectMapper.readValue(bytes, User.class);
        // } catch (JsonParseException e) {
        // e.printStackTrace();
        // } catch (JsonMappingException e) {
        // e.printStackTrace();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // return user;
        // }});
        // return operations.get(id);
    }

    @Override
    public User saveToRedis(Long id) {
        log.debug("Saving user to redis...");
        User user = new User();
        user.setId(id);
        user.setUserName("中文测试-UserName");
        UserGroup userGroup = new UserGroup();
        userGroup.setId(id);
        userGroup.setUserGroupName("中文测试-GroupName");
        user.setUserGroup(userGroup);
        try {
            log.debug("writeValueAsString: " + objectMapper.writeValueAsString(user));
            log.debug("writeValueAsBytes: "
                    + new String(objectMapper.writeValueAsBytes(user), charSet));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.opsForValue().set(id.toString(), user, 5, TimeUnit.MINUTES);
        // operations.set(id.toString(), user);
        return user;
    }

    @Override
    public UserProtos.User getFromRedisProtobuf(Long id) {
        log.debug("Getting user from redis in protobuf format...");
        redisTemplate.setValueSerializer(serializer);
        return (UserProtos.User)redisTemplate.opsForValue().get(id);
    }

    @Override
    public User saveToRedisProtobuf(Long id) {
        log.debug("Saving user to redis in protobuf format...");
        User user = new User();
        user.setId(id);
        user.setUserName("中文测试-UserName-protobuf");
        UserGroup userGroup = new UserGroup();
        userGroup.setId(id);
        userGroup.setUserGroupName("中文测试-GroupName-protobuf");
        user.setUserGroup(userGroup);
        //log.debug("protobuf result: "+serializer.serialize(toMessage(user)));
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.opsForValue().set(id.toString(), toMessage(user), 5, TimeUnit.MINUTES);
        return user;
    }

    private UserProtos.User toMessage(User user) {
        return UserProtos.User
                .newBuilder()
                .setId(user.getId())
                .setUserName(user.getUserName())
                .setUserGroup(
                        UserProtos.UserGroup.newBuilder().setId(user.getUserGroup().getId())
                                .setUserGroupName(user.getUserGroup().getUserGroupName())).build();
    }
}
