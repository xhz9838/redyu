package com.czxy.redyu.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/26
 */
@Data
public class ArchivesByYearAndMonthDTO {

    List<BasePostMinimalDTO> basePostMinimalDTOList;
}
