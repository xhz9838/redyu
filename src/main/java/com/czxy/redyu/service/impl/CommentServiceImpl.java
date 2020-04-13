package com.czxy.redyu.service.impl;

import cn.hutool.core.util.URLUtil;
import com.czxy.redyu.dao.CommentMapper;
import com.czxy.redyu.dao.PostMapper;
import com.czxy.redyu.exception.NotFoundException;
import com.czxy.redyu.model.dto.BaseCommentDTO;
import com.czxy.redyu.model.dto.BaseCommentWithParentVO;
import com.czxy.redyu.model.entity.Comment;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.params.BaseCommentParam;
import com.czxy.redyu.model.params.CommentQuery;
import com.czxy.redyu.model.projection.CommentChildrenCountProjection;
import com.czxy.redyu.model.vo.BaseCommentVO;
import com.czxy.redyu.model.vo.CommentWithHasChildrenVO;
import com.czxy.redyu.service.CommentService;
import com.czxy.redyu.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
@Slf4j
public class CommentServiceImpl<COMMENT extends Comment> implements CommentService<COMMENT> {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private CommentService commentService;

    @Resource
    private PostMapper postMapper;


    @Override
    public Integer getCommentCount() {
        return commentMapper.getCommentCount();
    }

    @Override
    public COMMENT createBy(BaseCommentParam<COMMENT> commentParam) {
        Assert.notNull(commentParam, "Comment param must not be null");
        // Validate the comment param manually
        ValidationUtils.validate(commentParam);
        COMMENT comment = commentParam.convertTo();
        // Check post id
        if (!ServiceUtils.isEmptyId(comment.getPostId())) {
            validateTarget(comment.getPostId());
        }

        // Check parent id
        if (!ServiceUtils.isEmptyId(comment.getParentId())) {
            mustExistById(comment.getParentId());
        }

        // Set some default values
        if (comment.getIpAddress() == null) {
            comment.setIpAddress(ServletUtils.getRequestIp());
        }

        if (comment.getUserAgent() == null) {
            comment.setUserAgent(ServletUtils.getHeaderIgnoreCase(HttpHeaders.USER_AGENT));
        }

        if (comment.getGravatarMd5() == null) {
            comment.setGravatarMd5(DigestUtils.md5DigestAsHex(comment.getEmail().getBytes()));
        }
        if (StringUtils.isNotEmpty(comment.getAuthorUrl())) {
            comment.setAuthorUrl(URLUtil.normalize(comment.getAuthorUrl()));
        }
        comment.setStatus(1);
        comment.setIsAdmin((byte) 1);
        comment.setTopPriority(0);
        comment.setContent(MarkdownUtils.renderHtml(comment.getContent()));
        commentMapper.insert(comment);
        return comment;
    }


    @Override
    public void validateTarget(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            throw new NotFoundException("查询不到该文章的信息").setErrorData(postId);
        }
//        if (post.getDisallowComment()) {
//            throw new BadRequestException("该文章已经被禁止评论").setErrorData(postId);
//        }
    }

    @Override
    public void mustExistById(Long id) {
        Comment commentByParentId = commentMapper.selectByPrimaryKey(id);
        if (commentByParentId == null) {
            throw new NotFoundException("父评论不存在");
        }
    }

    @Override
    public BaseCommentDTO convertTo(Comment comment) {
        Assert.notNull(comment, "Comment must not be null");

        return new BaseCommentDTO().convertFrom(comment);
    }

    @Override
    public List<BaseCommentWithParentVO> listCommentByPostId(Integer postId) {
        List<Comment> comments = commentMapper.listCommentByPostId(postId);

        // Get all comment parent ids
        Set<Long> parentIds = ServiceUtils.fetchProperty(comments, Comment::getParentId);
        if (parentIds.size() == 0) {
            HashSet<Long> parent = new HashSet<>(parentIds);
            parent.add(0L);
            parentIds = parent;
        }
        // Get all parent comments
        List<Comment> parentComments = commentMapper.listCommentById(parentIds);

        Map<Long, Comment> parentCommentMap = ServiceUtils.convertToMap(parentComments, Comment::getId);

        Map<Long, BaseCommentWithParentVO> parentCommentVoMap = new HashMap<>(parentCommentMap.size());

        return comments.stream().map(comment -> {
            // Convert to with parent vo
            BaseCommentWithParentVO commentWithParentVO = new BaseCommentWithParentVO().convertFrom(comment);

            // Get parent comment vo from cache
            BaseCommentWithParentVO parentCommentVo = parentCommentVoMap.get(comment.getParentId());
            if (parentCommentVo == null) {
                // Get parent comment
                Comment parentComment = parentCommentMap.get(comment.getParentId());

                if (parentComment != null) {
                    // Convert to parent comment vo
                    parentCommentVo = new BaseCommentWithParentVO().convertFrom(parentComment);
                    // Cache the parent comment vo
                    parentCommentVoMap.put(parentComment.getId(), parentCommentVo);
                }
            }
            // Set parent
            commentWithParentVO.setParent(parentCommentVo == null ? null : parentCommentVo.clone());

            return commentWithParentVO;
        }).collect(Collectors.toList());

    }

    @Override
    public List<BaseCommentVO> pageVosBy(Integer postId, Integer pageNum) {
        Assert.notNull(postId, "Post id must not be null");

        log.debug("Getting comment tree view of post: [{}], page info: [{}]", postId, pageNum);


        List<Comment> comments = commentMapper.listCommentByPostId(postId);

        return pageVosBy(comments);
    }

    @Override
    public <T extends BaseCommentDTO> Page<T> filterIpAddress(Page<T> commentPage) {
        return null;
    }

    @Override
    public PageInfo<BaseCommentVO> pageVosByPage(Integer postId, Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<Comment> commentList = commentMapper.pageVosByPage(postId);

        PageInfo<Comment> commentPageInfo = new PageInfo<>(commentList);

        List<BaseCommentVO> baseCommentVOS = commentPageInfo.getList().stream().map(this::convertPage).collect(Collectors.toList());

        baseCommentVOS.forEach(comment -> {
            List<Comment> commentChildList = commentMapper.findChildCommentById(comment.getId());
            List<BaseCommentVO> childrenList = commentChildList.stream().map(this::convertPage).collect(Collectors.toList());
            comment.setChildren(childrenList);
        });

        PageInfo<BaseCommentVO> baseCommentVOPageInfo = new PageInfo<>();
        baseCommentVOPageInfo = BeanUtils.transformFrom(commentPageInfo, baseCommentVOPageInfo.getClass());
        if (baseCommentVOPageInfo != null) {
            baseCommentVOPageInfo.setList(baseCommentVOS);
        }
        return baseCommentVOPageInfo;
    }

    @Override
    public BaseCommentVO convertPage(Comment comment) {
        return new BaseCommentVO().convertFrom(comment);
    }

    @Override
    public PageInfo<BaseCommentDTO> commentDTOList(CommentQuery commentQuery) {
        Assert.notNull(commentQuery, "查询参数不能为null");
        PageHelper.startPage(commentQuery.getPageNum(), commentQuery.getPageSize());
        if (commentQuery.getKeyword() != null && !"".equals(commentQuery.getKeyword())) {
            commentQuery.setKeyword("%" + commentQuery.getKeyword() + "%");
        }
        List<Comment> commentList = commentMapper.findAllCommentByParams(commentQuery);

        PageInfo<Comment> commentPageInfo = new PageInfo<>(commentList);
        PageInfo<BaseCommentDTO> baseCommentDTOPageInfo = new PageInfo<>();
        BeanUtils.updateProperties(commentPageInfo, baseCommentDTOPageInfo);
        List<BaseCommentDTO> baseCommentDTOList = commentPageInfo.getList().stream().map(this::convertTo).collect(Collectors.toList());
        baseCommentDTOPageInfo.setList(baseCommentDTOList);
        return baseCommentDTOPageInfo;
    }

    @Override
    public void updateStatusByCid(Integer id, Integer status) {
        commentMapper.updateStatusByCid(id, status);
    }

    @Override
    public void deleteComment(Integer id) {
        commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delManyComment(List<Integer> ids) {
        ids.stream().forEach(id -> {
            commentMapper.deleteByPrimaryKey(id);
        });
    }

    @Override
    public PageInfo<CommentWithHasChildrenVO> pageTopCommentsBy(Integer targetId, Integer status, Integer pageNum, Integer pageSize) {
        Assert.notNull(targetId, "Target id must not be null");
        Assert.notNull(status, "Comment status must not be null");


        // Get all comments
        PageHelper.startPage(pageNum, pageSize);
        List<COMMENT> topCommentPage = commentMapper.findAllByPostIdAndStatusAndParentId(targetId, status, 0L);
        PageInfo<COMMENT> commentPageInfo = new PageInfo<>(topCommentPage);

        if (topCommentPage.isEmpty()) {
            // If the comments is empty
            return new PageInfo<>();
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
        commentWithHasChildrenVOS= commentWithHasChildrenVOS.stream().map(commentWithHasChildrenVO -> {
            commentWithHasChildrenVO.setComments(listChildrenBy(targetId, commentWithHasChildrenVO.getId(), 1));
            return commentWithHasChildrenVO;
        }).collect(Collectors.toList());
        PageInfo<CommentWithHasChildrenVO> commentWithHasChildrenVOPageInfo = new PageInfo<>();
        BeanUtils.updateProperties(commentPageInfo, commentWithHasChildrenVOPageInfo);
        commentWithHasChildrenVOPageInfo.setList(commentWithHasChildrenVOS);

        return commentWithHasChildrenVOPageInfo;
    }

    @Override
    public List<Comment> listChildrenBy(Integer postId, Long parentId, Integer status) {
        Assert.notNull(parentId, "Target id must not be null");
        Assert.notNull(parentId, "Comment parent id must not be null");


        // Get comments recursively

        // Get direct children
        List<Comment> directChildren = commentMapper.findAllByPostIdAndStatusAndParentId(postId, status, parentId);

        // Create result container
        Set<Comment> children = new HashSet<>();

        // Get children comments
        getChildrenRecursively(directChildren, status, children);

        // Sort children
        List<Comment> childrenList = new ArrayList<>(children);
        childrenList.sort(Comparator.comparing(Comment::getId));

        return childrenList;
    }

    @Override
    public List<BaseCommentDTO> convertTo(List<COMMENT> comments) {
        if (CollectionUtils.isEmpty(comments)) {
            return Collections.emptyList();
        }
        return comments.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseCommentDTO> hotComment() {
       List<Comment> commentList =  commentMapper.hotComment();
        return commentList.stream().map(this::convertTo).collect(Collectors.toList());

    }

    /**
     * Get children comments recursively.
     *
     * @param topComments top comment list
     * @param status      comment status must not be null
     * @param children    children result must not be null
     */
    private void getChildrenRecursively(@Nullable List<Comment> topComments, Integer status, @NonNull Set<Comment> children) {
        Assert.notNull(status, "Comment status must not be null");
        Assert.notNull(children, "Children comment set must not be null");

        if (CollectionUtils.isEmpty(topComments)) {
            return;
        }

        // Convert comment id set
        Set<Long> commentIds = ServiceUtils.fetchProperty(topComments, Comment::getId);

        // Get direct children
        List<Comment> directChildren = commentMapper.findAllByStatusAndParentIdIn(status, commentIds);

        // Recursively invoke
        getChildrenRecursively(directChildren, status, children);

        // Add direct children to children result
        children.addAll(topComments);
    }

    private List<BaseCommentVO> pageVosBy(List<Comment> comments) {

        Assert.notNull(comments, "Comments must not be null");
        List<BaseCommentVO> topComments = convertToVo(comments);


        return topComments;

    }

    private List<BaseCommentVO> convertToVo(List<Comment> comments) {
        // Init the top virtual comment
        BaseCommentVO topVirtualComment = new BaseCommentVO();
        topVirtualComment.setId(0L);
        topVirtualComment.setChildren(new LinkedList<>());
        concreteTree(topVirtualComment, new LinkedList<>(comments));
        return topVirtualComment.getChildren();
    }

    private void concreteTree(BaseCommentVO parentComment, LinkedList<Comment> comments) {
        if (CollectionUtils.isEmpty(comments)) {
            return;
        }

        // Get children
        List<Comment> children = comments.stream()
                .filter(comment -> Objects.equals(parentComment.getId(), comment.getParentId()))
                .collect(Collectors.toList());

        // Add children
        children.forEach(comment -> {
            // Convert to comment vo
            BaseCommentVO commentVO = new BaseCommentVO().convertFrom(comment);

            if (parentComment.getChildren() == null) {
                parentComment.setChildren(new LinkedList<>());
            }
            parentComment.getChildren().add(commentVO);
        });

        // Remove children
        comments.removeAll(children);

        if (!CollectionUtils.isEmpty(parentComment.getChildren())) {
            // Recursively concrete the children
            parentComment.getChildren().forEach(childComment -> concreteTree(childComment, comments));

        }
    }
}
