package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.PostMapper;
import com.czxy.redyu.dao.VisitLogDao;
import com.czxy.redyu.model.entity.VisitLog;
import com.czxy.redyu.model.support.Address;
import com.czxy.redyu.model.support.IpResult;
import com.czxy.redyu.service.VisitLogService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<VisitLog> visitLog() {
        return visitLogDao.selectAll();
    }

    @Override
    public void addVisitLog(VisitLog visitLog, String requestIp) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<IpResult> entity = restTemplate.getForEntity("https://apis.map.qq.com/ws/location/v1/ip?ip=" + requestIp + "&key=BDCBZ-5TU2P-XPTDR-LILPD-4A4DK-L5FZQ", IpResult.class);
        if (entity.getBody() != null && entity.getBody().getResult() != null && entity.getBody().getResult().getAd_info() != null) {
            Address ad_info = entity.getBody().getResult().getAd_info();

            visitLog.setAddress(ad_info.getNation() + ad_info.getProvince() + ad_info.getCity());
        }
        visitLogDao.insert(visitLog);
    }
}