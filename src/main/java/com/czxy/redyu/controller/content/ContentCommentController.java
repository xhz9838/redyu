package com.czxy.redyu.controller.content;

import com.czxy.redyu.exception.BadRequestException;
import com.czxy.redyu.model.dto.BaseCommentDTO;
import com.czxy.redyu.model.entity.Comment;
import com.czxy.redyu.model.params.PostCommentParam;
import com.czxy.redyu.model.vo.BaseCommentVO;
import com.czxy.redyu.model.vo.CommentWithHasChildrenVO;
import com.czxy.redyu.service.CommentService;
import com.czxy.redyu.utils.RedisUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/22
 */
@RequestMapping("/content/comment")
@RestController
public class ContentCommentController {

    @Resource
    private CommentService commentService;
    
    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/avatar/{email}")
    public String avatar(@PathVariable String email){

        //TODO 后期优化  存到数据库
        String baseUrl = "https://cdn.v2ex.com/gravatar/";
        return baseUrl+DigestUtils.md5DigestAsHex(email.getBytes())+"?s=65&r=G&d=";
    }

    @PostMapping("/createComment")
    public BaseCommentDTO comment(@RequestBody PostCommentParam postCommentParam, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String ip = (String) redisUtil.get(remoteAddr);
        if(ip!=null){
            throw new BadRequestException("评论过快，请稍后操作");
        }
        redisUtil.set(remoteAddr,"评论缓存",10);

        return commentService.convertTo(commentService.createBy(postCommentParam));
    }
//    @GetMapping("/listCommentByPostId/{postId}")
//    public List<BaseCommentWithParentVO> listCommentByPostId(@PathVariable Integer postId){
//      return   commentService.listCommentByPostId(postId);
//    }
// TODO 没用到
    @GetMapping("/listCommentByPostId/{postId}")
    public List<BaseCommentVO> listCommentsTree(@PathVariable("postId") Integer postId,
                                                @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return commentService.pageVosBy(postId,pageNum);

    }
    // TODO 没用到
    @GetMapping("/commentByPostIdForPage/{postId}")
    public PageInfo<BaseCommentVO> listCommentTreeForPage(@PathVariable("postId") Integer postId,
                                                          @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return commentService.pageVosByPage(postId,pageNum);

    }

    /**
     * 查询评论状态书，多级子评论统一在第二级展示
     * @param postId
     * @param pageNum
     * @return
     */

    @GetMapping("{postId}/comments/treeView")
    public PageInfo<CommentWithHasChildrenVO> listTopComments(@PathVariable("postId") Integer postId,
                                                          @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNum) {

      return commentService.pageTopCommentsBy(postId,1, pageNum,10);

    }

    // TODO 没用到
    @GetMapping("/{postId}/{parentId}/children")
    public List<BaseCommentDTO> listChildrenBy(@PathVariable("postId") Integer postId,
                                               @PathVariable("parentId") Long parentId){
        // Find all children comments
        List<Comment> postComments = commentService.listChildrenBy(postId, parentId, 1);
        // Convert to base comment dto

       return   commentService.convertTo(postComments);

    }

    @GetMapping("/hotComment")
    public List<BaseCommentDTO> hotComment(){
        return commentService.hotComment();
    }
}
