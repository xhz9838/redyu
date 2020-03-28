package com.czxy.redyu.model.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.io.Serializable;

/**
 * (VisitLog)实体类
 *
 * @author makejava
 * @since 2020-03-28 11:47:57
 */
@Data
@ToString
public class VisitLog implements Serializable {
    private static final long serialVersionUID = 823641761949991151L;
    
    private Integer id;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String username;
    
    private String ipAddress;
    
    private String content;
    
    private Integer type;

    private String userAgent;
}