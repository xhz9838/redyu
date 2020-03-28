package com.czxy.redyu.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/26
 */
@Data
@ToString
public class BlogAdminLineChart {

     private List<Integer>   newVisitis;

     private List<Integer> postCounts;

     private List<Integer> comments;
}
