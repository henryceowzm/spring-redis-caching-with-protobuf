package com.henry.spring.redis;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

  private static final Logger log = Logger.getLogger(ExampleController.class);
  private static final AtomicLong counter = new AtomicLong();

  private UserRepository userRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
  @ResponseBody
  public User getUser(@PathVariable Long id) {
    return userRepository.getUser(id);
  }

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  @ResponseBody
  public User saveUser() {
    return userRepository.saveUser(counter.getAndIncrement());
  }

  @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public void deleteUser(@PathVariable Long id) {
    userRepository.deleteUser(id);
  }
  
  @RequestMapping(value = "/users/from-redis/{id}", method = RequestMethod.GET)
  @ResponseBody
  public User getFromRedis(@PathVariable Long id) {
    return userRepository.getFromRedis(id);
  }
  
  @RequestMapping(value = "/users/to-redis/{id}", method = RequestMethod.GET)
  @ResponseBody
  public User saveToRedis(@PathVariable Long id) {
    return userRepository.saveToRedis(id);
  }
  
  @RequestMapping(value = "/users/from-redis-protobuf/{id}", method = RequestMethod.GET)
  @ResponseBody
  public UserProtos.User getFromRedisProtobuf(@PathVariable Long id) {
    return userRepository.getFromRedisProtobuf(id);
  }
  
//  @RequestMapping(value = "/users/from-redis-protobuf/{id}", method = RequestMethod.GET)
//  @ResponseBody
//  public ResponseEntity<UserProtos.User> getFromRedisProtobuf(@PathVariable Long id) {
//    return ResponseEntity.ok(userRepository.getFromRedisProtobuf(id));
//  }
  
  @RequestMapping(value = "/users/to-redis-protobuf/{id}", method = RequestMethod.GET)
  @ResponseBody
  public User saveToRedisProtobuf(@PathVariable Long id) {
    return userRepository.saveToRedisProtobuf(id);
  }
}