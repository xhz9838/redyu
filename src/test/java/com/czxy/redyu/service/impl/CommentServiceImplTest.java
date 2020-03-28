package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.CommentMapper;
import com.czxy.redyu.model.entity.Comment;
import com.czxy.redyu.model.projection.CommentChildrenCountProjection;
import com.czxy.redyu.model.vo.BaseCommentVO;
import com.czxy.redyu.model.vo.CommentWithHasChildrenVO;
import com.czxy.redyu.utils.BeanUtils;
import com.czxy.redyu.utils.ServiceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentServiceImplTest {
    @Resource
    private CommentMapper commentMapper;


    @Test
    public void listCommentByPostId() {

        List<Comment> comments = commentMapper.listCommentByPostId(39);


        // Get all comment parent ids
        Set<Long> parentIds = ServiceUtils.fetchProperty(comments, Comment::getParentId);

        // Get all parent comments
        List<Comment> list = commentMapper.listCommentById(parentIds);
        System.out.println(list);
    }

    @Test
    public void pageVosByPage() {
        PageHelper.startPage(1,10);
        List<Comment> commentList = commentMapper.pageVosByPage(42);

        PageInfo<Comment> commentPageInfo = new PageInfo<>(commentList);

        List<BaseCommentVO> baseCommentVOS = commentPageInfo.getList().stream().map(this::convertPage).collect(Collectors.toList());

        baseCommentVOS.forEach(comment -> {
            List<Comment> commentChildList= commentMapper.findChildCommentById(comment.getId());
            List<BaseCommentVO> childrenList = commentChildList.stream().map(this::convertPage).collect(Collectors.toList());
            comment.setChildren(childrenList);
        });

        PageInfo<BaseCommentVO> baseCommentVOPageInfo = new PageInfo<>();
        baseCommentVOPageInfo= BeanUtils.transformFrom(commentPageInfo, baseCommentVOPageInfo.getClass());
        if(baseCommentVOPageInfo!=null){
            baseCommentVOPageInfo.setList(baseCommentVOS);

        }
        System.out.println(baseCommentVOPageInfo);
    }
    public BaseCommentVO convertPage(Comment comment) {
        return new BaseCommentVO().convertFrom(comment);
    }

    @Test
    public void pageTopCommentsBy() {
        // Get all comments
        PageHelper.startPage(1,5);
        List<Comment> topCommentPage = commentMapper.findAllByPostIdAndStatusAndParentId(42, 1, 0L);
        PageInfo<Comment> commentPageInfo = new PageInfo<>(topCommentPage);
        if (topCommentPage.isEmpty()) {
            // If the comments is empty
            System.out.println(topCommentPage);
        }


        // Get top comment ids
        Set<Long> topCommentIds = ServiceUtils.fetchProperty(commentPageInfo.getList(), Comment::getId);

        // Get direct children count
//        topCommentIds.forEach(id->{
//            commentMapper.findDirectChildrenCount(id);
//        });
        List<CommentChildrenCountProjection> directChildrenCount = commentMapper.findDirectChildrenCount(topCommentIds);

        // Convert to comment - children count map
        Map<Long, Long> commentChildrenCountMap = ServiceUtils.convertToMap(directChildrenCount, CommentChildrenCountProjection::getCommentId, CommentChildrenCountProjection::getDirectChildrenCount);

        // Convert to comment with has children vo

        List<CommentWithHasChildrenVO> commentWithHasChildrenVOS = commentPageInfo.getList().stream().map(topComment -> {
            CommentWithHasChildrenVO comment = new CommentWithHasChildrenVO().convertFrom(topComment);
            comment.setHasChildren(commentChildrenCountMap.getOrDefault(topComment.getId(), 0L) > 0);
            return comment;
        }).collect(Collectors.toList());
        PageInfo<CommentWithHasChildrenVO> commentWithHasChildrenVOPageInfo = new PageInfo<>();
        BeanUtils.updateProperties(commentPageInfo,commentWithHasChildrenVOPageInfo);
        commentWithHasChildrenVOPageInfo.setList(commentWithHasChildrenVOS);
        System.out.println(commentWithHasChildrenVOPageInfo);
    }
}