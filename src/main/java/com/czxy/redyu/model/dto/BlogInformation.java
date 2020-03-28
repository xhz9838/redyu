package com.czxy.redyu.model.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/21
 */
@Data
@ToString
public class BlogInformation {

    private Integer postCount;

    private Integer commentCount;

    private Integer OperatingTime;

    private Long visits;


    private LocalDateTime finalActivity;
}
