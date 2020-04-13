package com.czxy.redyu.service;

import com.czxy.redyu.model.entity.VisitLog;

import java.util.List;

/**
 * (VisitLog)表服务接口
 *
 * @author makejava
 * @since 2020-03-28 11:47:58
 */
public interface VisitLogService {


    List<VisitLog> visitLog();

    void addVisitLog(VisitLog visitLog, String requestIp);
}