package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.vo.CommentWithHasChildrenVO;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/16
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ArchiveDetailDTO extends BasePostSimpleDTO{

    /**
     * 格式化后的内容
     */
    private String formatContent;

    /**
     * 原始内容
     */
    private String originalContent;


    List<BasePostMinimalDTO> nextAndLast;


    PageInfo<CommentWithHasChildrenVO> comment;
}
