package com.czxy.redyu.service;

import com.czxy.redyu.model.dto.BaseCommentDTO;
import com.czxy.redyu.model.dto.BaseCommentWithParentVO;
import com.czxy.redyu.model.entity.Comment;
import com.czxy.redyu.model.params.BaseCommentParam;
import com.czxy.redyu.model.params.CommentQuery;
import com.czxy.redyu.model.vo.BaseCommentVO;
import com.czxy.redyu.model.vo.CommentWithHasChildrenVO;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface CommentService <COMMENT extends Comment>{
    Integer getCommentCount();


    @NonNull
    COMMENT createBy(@NonNull BaseCommentParam<COMMENT> commentParam);


    void validateTarget(@NonNull Integer targetId);

    void mustExistById(@NonNull Long id);


    BaseCommentDTO convertTo(@NonNull COMMENT comment);

    List<BaseCommentWithParentVO> listCommentByPostId(Integer postId);

    List<BaseCommentVO>  pageVosBy(Integer postId, Integer pageNum);


    <T extends BaseCommentDTO> Page<T> filterIpAddress(@NonNull Page<T> commentPage);

    PageInfo<BaseCommentVO> pageVosByPage(Integer postId, Integer pageNum);

    BaseCommentVO convertPage(Comment comment);

    PageInfo<BaseCommentDTO> commentDTOList(CommentQuery commentQuery);

    void updateStatusByCid(Integer id, Integer status);

    void deleteComment(Integer id);

    void delManyComment(List<Integer> ids);

    PageInfo<CommentWithHasChildrenVO> pageTopCommentsBy(Integer postId,Integer status ,Integer pageNum, Integer pageSize);

    List<Comment> listChildrenBy(Integer postId, Long parentId, Integer status);
    @NonNull
    List<BaseCommentDTO> convertTo(@NonNull List<COMMENT> comments);

    List<BaseCommentDTO> hotComment();
}
