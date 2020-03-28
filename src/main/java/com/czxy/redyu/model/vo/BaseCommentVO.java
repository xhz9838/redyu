package com.czxy.redyu.model.vo;

import com.czxy.redyu.model.dto.BaseCommentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/23
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseCommentVO extends BaseCommentDTO {

    List<BaseCommentVO> children;
}
