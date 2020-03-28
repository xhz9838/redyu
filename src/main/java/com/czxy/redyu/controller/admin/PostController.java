package com.czxy.redyu.controller.admin;

import com.czxy.redyu.model.dto.BasePostMinimalDTO;
import com.czxy.redyu.model.dto.BasePostSimpleDTO;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.enums.PostStatus;
import com.czxy.redyu.model.params.PostParam;
import com.czxy.redyu.model.params.PostQuery;
import com.czxy.redyu.model.vo.PostDetailVO;
import com.czxy.redyu.service.PostService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@RestController
@RequestMapping("/admin/post")
public class PostController {

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/createPost")
    public PostDetailVO createBy(@Valid @RequestBody PostParam postParam) {
        //转换
        Post post = postParam.convertTo();

        return postService.createBy(post,postParam.getTagIds(),postParam.getCategoryIds());
    }

    @PostMapping
    public PageInfo<? extends BasePostSimpleDTO> postForPage(@RequestBody PostQuery postQuery){
       return postService.listByPage(postQuery);

    }

    @GetMapping("/{postId}")
    public PostDetailVO postById(@PathVariable Integer postId){
        Post post = postService.getById(postId);
        return postService.convertToDetailVo(post);
    }

    @PutMapping("{postId}/status/{status}")
    public BasePostMinimalDTO updateStatusBy(@PathVariable("postId") Integer postId, @PathVariable("status")PostStatus status){
        Post post = postService.updateStatus(status, postId);

        return new BasePostMinimalDTO().convertFrom(post);
    }
    @DeleteMapping("/{postId}")
    public void deletePostById(@PathVariable Integer postId){
        postService.deletePostById(postId);
    }


}
