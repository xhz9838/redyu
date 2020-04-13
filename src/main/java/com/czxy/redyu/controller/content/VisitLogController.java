package com.czxy.redyu.controller.content;

import com.czxy.redyu.model.entity.VisitLog;
import com.czxy.redyu.service.VisitLogService;
import com.czxy.redyu.utils.ServletUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * (VisitLog)表控制层
 *
 * @author xuhongzu
 * @since 2020-03-28 11:47:58
 */
@RestController
@RequestMapping("/content/visitLog")
public class VisitLogController {
    /**
     * 服务对象
     */
    @Resource
    private VisitLogService visitLogService;


    @GetMapping("/{title}")
    public void visitLog(HttpServletRequest request, @PathVariable String title){
        String username = ServletUtils.getHeaderIgnoreCase("username");
        String userAgent = ServletUtils.getHeaderIgnoreCase("User-Agent");
        String requestIp = ServletUtils.getRequestIp();
        VisitLog visitLog = new VisitLog();
        visitLog.setUsername(username);
        visitLog.setContent("访问文章"+title);
        visitLog.setIpAddress(requestIp);
        visitLog.setUserAgent(userAgent);
        visitLog.setType(1);
        visitLogService.addVisitLog(visitLog,requestIp);
    }
}