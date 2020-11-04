package com.czxy.redyu.security.filter;

import com.czxy.redyu.exception.AuthenticationException;
import com.czxy.redyu.model.dto.UserDTO;
import com.czxy.redyu.security.handler.AdminAuthenticationFailureHandler;
import com.czxy.redyu.utils.JwtUtils;
import com.czxy.redyu.utils.RasUtils;
import com.czxy.redyu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/15
 */
@Slf4j
@Component
public class AdminAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathMatcher antPathMatcher;
    @Value("${redyu.ras.pub}")
    private  String pubKeyPath ;

    @Value("${redyu.ras.pri}")
    private  String priKeyPath;

    @Resource
    private RedisUtil redisUtil;
    /**
     * Admin token header name.
     */
    public final static String ADMIN_TOKEN_HEADER_NAME = "ADMIN-" + HttpHeaders.AUTHORIZATION;

    /**
     * Admin token param name.
     */
    private final static String ADMIN_TOKEN_QUERY_NAME = "admin_token";
    @Resource
    private AdminAuthenticationFailureHandler adminAuthenticationFailureHandler;

    private HashSet<String> excludeUrlPatterns = new HashSet<>(2);

    public AdminAuthenticationFilter() {
        antPathMatcher = new AntPathMatcher();
    }


    @PostConstruct
    public void createRas() throws Exception {
        log.info("生成ras成功");
        RasUtils.generateKey(pubKeyPath, priKeyPath, UUID.randomUUID().toString());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Assert.notNull(request, "Http请求不能为空");
        return excludeUrlPatterns.stream().anyMatch(p -> antPathMatcher.match(p, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get token from request
        String token = getTokenFromRequest(request);
        if (StringUtils.isBlank(token)) {
            adminAuthenticationFailureHandler
                    .onFailure(request, response, new AuthenticationException("未登录，请登录后访问"));

            return;
        }
        try {
            Object redis = redisUtil.get(token);
            UserDTO store = redis instanceof UserDTO ? ((UserDTO) redis) : null;
           // log.info("redis取出缓存的用户={}", store);
            UserDTO userDTO = JwtUtils.getObjectFromToken(token, RasUtils.getPublicKey(pubKeyPath), UserDTO.class);
           // log.info("传入用户={}", userDTO);
            if (!userDTO.equals(store)) {
                adminAuthenticationFailureHandler.onFailure(request, response, new AuthenticationException("token验证失败"));
                return;
            }
        } catch (Exception e) {
            adminAuthenticationFailureHandler.onFailure(request, response, new AuthenticationException("token验证失败"));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(@NonNull HttpServletRequest request) {
        Assert.notNull(request, "Http请求不能为空");

        String token = request.getHeader(ADMIN_TOKEN_HEADER_NAME);

        if (StringUtils.isBlank(token)) {
            token = request.getParameter(ADMIN_TOKEN_QUERY_NAME);

               log.debug("Got token from parameter: [{}: {}]", ADMIN_TOKEN_QUERY_NAME, token);
        } else {
             log.debug("Got token from header: [{}: {}]", ADMIN_TOKEN_HEADER_NAME, token);
        }

        return token;
    }

    public void addExcludeUrlPattens(String... excludeUrlPattens) {
        Assert.notNull(excludeUrlPattens, "排除的url不能为空");
        Collections.addAll(excludeUrlPatterns, excludeUrlPattens);
    }

    public HashSet<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(Collection<String> excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "排除url不能为空");
        this.excludeUrlPatterns = new HashSet<>(excludeUrlPatterns);
    }
}
