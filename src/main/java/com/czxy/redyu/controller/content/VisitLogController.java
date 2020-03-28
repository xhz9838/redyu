package com.czxy.redyu.controller.content;

import com.czxy.redyu.service.VisitLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (VisitLog)表控制层
 *
 * @author makejava
 * @since 2020-03-28 11:47:58
 */
@RestController
@RequestMapping("visitLog")
public class VisitLogController {
    /**
     * 服务对象
     */
    @Resource
    private VisitLogService visitLogService;



}