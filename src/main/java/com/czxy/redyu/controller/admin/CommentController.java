package com.czxy.redyu.controller.admin;

import com.czxy.redyu.model.dto.BaseCommentDTO;
import com.czxy.redyu.model.params.CommentQuery;
import com.czxy.redyu.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@RestController
@RequestMapping("/admin/comment")
public class CommentController {

    @Resource
    private CommentService commentService;


    /**
     * 查询所有评论
     * @return
     */
    @PostMapping("/comments")
    public PageInfo<BaseCommentDTO> commentDTOList(@RequestBody CommentQuery commentQuery){
        return commentService.commentDTOList(commentQuery);
    }
    /**
     * 改变评论状态
     * @return
     */
    @PutMapping("/updateStatusByCid/{id}/{status}")
    public void updateStatusByCid(@PathVariable Integer id,@PathVariable Integer status){
         commentService.updateStatusByCid(id,status);
    }
    /**
     * 删除评论
     * @return
     */
    @DeleteMapping("/deleteComment/{id}")
    public void deleteComment(@PathVariable Integer id){
         commentService.deleteComment(id);
    }
    /**
     * 批量删除评论
     * @return
     */
    @PostMapping("/deleteComment")
    public void delManyComment(@RequestBody List<Integer> ids){
        commentService.delManyComment(ids);
    }
}
