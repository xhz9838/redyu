package com.czxy.redyu.controller;

import com.czxy.redyu.model.entity.User;
import com.czxy.redyu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/18
 */
@Slf4j
@RestController
@RequestMapping("/redis")
public class TestController {
    private static int ExpireTime = 60;

    @Resource
    private RedisUtil redisUtil;
    @GetMapping("/set")
    public boolean redisSet(@RequestParam String key, @RequestParam String value){
        User user = new User();
        user.setUsername(value);

        return redisUtil.set(key,user);
    }
    @GetMapping("/get")
    public Object redisGet(@RequestParam String key){
        System.out.println(redisUtil.get(key));
        return redisUtil.get(key);
    }


}
