package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.PostMapper;
import com.czxy.redyu.dao.VisitLogDao;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.entity.VisitLog;
import com.czxy.redyu.service.VisitLogService;
import com.czxy.redyu.utils.ServletUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * (VisitLog)表服务实现类
 *
 * @author makejava
 * @since 2020-03-28 11:47:58
 */
@Service
@Transactional
@Aspect
public class VisitLogServiceImpl implements VisitLogService {
    @Resource
    private VisitLogDao visitLogDao;

    @Resource
    private PostMapper postMapper;

    @Pointcut("execution(* com.czxy.redyu.controller.content.ContentArchiveController.*(..))")
    public void archiveForPage() {

    }


    @Around("archiveForPage()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        String username = ServletUtils.getHeaderIgnoreCase("username");
        String userAgent = ServletUtils.getHeaderIgnoreCase("User-Agent");
        String requestIp = ServletUtils.getRequestIp();
        VisitLog visitLog = new VisitLog();
        if(methodName.equals("archiveForPage")){
            visitLog.setContent("访问首页");
             visitLog.setType(0);
            visitLog.setIpAddress(requestIp);

            visitLog.setUserAgent(userAgent);
            visitLog.setUsername(username);
            visitLogDao.insert(visitLog);

        }else if(methodName.equals("archiveByUrl")){
            if (args[0] instanceof String) {
                Post post = postMapper.archiveByUrl((String) args[0]);
                visitLog.setContent("访问文章："+post.getTitle());
                visitLog.setType(1);
                visitLog.setIpAddress(requestIp);

                visitLog.setUserAgent(userAgent);
                visitLog.setUsername(username);
                visitLogDao.insert(visitLog);
            }

        }

        Object returnObj = joinPoint.proceed();
        return returnObj;
    }

}