package com.czxy.redyu.controller.admin;

import com.czxy.redyu.model.dto.BlogAdminLineChart;
import com.czxy.redyu.model.dto.BlogInformation;
import com.czxy.redyu.model.dto.UserDTO;
import com.czxy.redyu.model.params.LoginParam;
import com.czxy.redyu.security.token.AuthToken;
import com.czxy.redyu.service.UserService;
import com.czxy.redyu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@RestController
@RequestMapping("/admin/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public AuthToken authToken(@RequestBody LoginParam loginParam, HttpServletRequest request) {
        return userService.authenticate(loginParam);
    }

    @GetMapping("/info")
    public UserDTO userInfo(@RequestParam("token") String token, HttpServletRequest request) {
        return (UserDTO) redisUtil.get(token);
    }
    @PostMapping("/logout")
    public String logout(@RequestParam("token") String token, HttpServletRequest request) {
        redisUtil.del(token);
        return "退出登录成功";
    }

    @GetMapping("/blogInformation")
    public BlogInformation blogInformation(){
        return userService.blogInformation();
    }

    @GetMapping("/visitsLastWeek")
    public BlogAdminLineChart blogAdminLineChart(){
        return userService.blogAdminLineChart();
    }
}
