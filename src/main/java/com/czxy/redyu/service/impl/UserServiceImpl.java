package com.czxy.redyu.service.impl;

import cn.hutool.core.lang.Validator;
import com.czxy.redyu.config.BCrypt;
import com.czxy.redyu.dao.UserMapper;
import com.czxy.redyu.exception.BadRequestException;
import com.czxy.redyu.exception.NotFoundException;
import com.czxy.redyu.model.dto.BlogAdminLineChart;
import com.czxy.redyu.model.dto.BlogInformation;
import com.czxy.redyu.model.dto.UserDTO;
import com.czxy.redyu.model.entity.User;
import com.czxy.redyu.model.entity.VisitLog;
import com.czxy.redyu.model.params.LoginParam;
import com.czxy.redyu.security.token.AuthToken;
import com.czxy.redyu.service.*;
import com.czxy.redyu.utils.JwtUtils;
import com.czxy.redyu.utils.RasUtils;
import com.czxy.redyu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private PostService postService;



    @Resource
    private CommentService commentService;

    @Resource
    private OptionService optionService;


    @Resource
    private VisitLogService visitLogService;

    @Value("${redyu.ras.pub}")
    private  String pubKeyPath ;

    @Value("${redyu.ras.pri}")
    private  String priKeyPath;

    @Resource
    private RedisUtil redisUtil;

    public boolean passwordMatch(User user, String plainPassword) {
        return !StringUtils.isBlank(plainPassword) && BCrypt.checkpw(plainPassword, user.getPassword());
    }

    public User findUserByUsername(String username){
        return Optional.ofNullable(userMapper.findUserByUsername(username)).orElseThrow(()-> new NotFoundException("用户名密码不匹配"));

    }
    public User findUserByEmail(String username){
        return Optional.ofNullable(userMapper.findUserByEmail(username)).orElseThrow(()-> new NotFoundException("用户名密码不匹配"));

    }
    @Override
    public AuthToken authenticate(@NonNull LoginParam loginParam) {
        Assert.notNull(loginParam,"登录参数不能为空");
        String message = "用户名或密码错误";
        String username = loginParam.getUsername();
        User user = null;
        try {
            user = Validator.isEmail(username) ?
                    findUserByEmail(username) : findUserByUsername(username);
        } catch (NotFoundException e) {
            throw new BadRequestException(message);
        }
        if(!passwordMatch(user,loginParam.getPassword())){
            throw new BadRequestException(message);
        }
        return buildAuthToken(user);
    }

    @Override
    public BlogInformation blogInformation() {
        BlogInformation blogInformation = new BlogInformation();
        blogInformation.setPostCount(postService.getPostCount());
        blogInformation.setCommentCount(commentService.getCommentCount());
        blogInformation.setVisits(postService.getAllVisits());
        blogInformation.setOperatingTime(optionService.OperatingTime());
        blogInformation.setFinalActivity(postService.getFinalActivity());
        return blogInformation;
    }

    @Override
    public BlogAdminLineChart blogAdminLineChart() {
        LocalDate nowDay = LocalDate.now().plusDays(-10);
        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            days.add(postService.getVisitsByDays(nowDay.toString()));
            nowDay = nowDay.plusDays(1);
        }

        return null;
    }

    @Override
    public List<VisitLog> visitLog() {
        return visitLogService.visitLog();
    }

    public AuthToken buildAuthToken(User user) {
        int tokenExpireTime = 60*24*30;
        AuthToken authToken = new AuthToken();
        String token = "";
        try {
            UserDTO userDTO = new UserDTO().convertFrom(user);
            token = JwtUtils.generateToken(userDTO, tokenExpireTime, RasUtils.getPrivateKey(priKeyPath));
            authToken.setAccessToken(token);
            authToken.setExpiredIn(tokenExpireTime);
            redisUtil.set(token,userDTO,60*60*24*7);
            log.info("Redis缓存用户信息，userDTO={}",redisUtil.get(token));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("token生成失败");
        }
        return authToken;
    }
}
