package com.czxy.redyu.controller.admin;

import com.czxy.redyu.model.dto.TagDTO;
import com.czxy.redyu.model.entity.Tag;
import com.czxy.redyu.model.params.TagParam;
import com.czxy.redyu.service.PostTagService;
import com.czxy.redyu.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@RestController
@RequestMapping("/admin/tag")
public class TagController  {
    @Resource
    private PostTagService postTagService;

    @Resource
    private TagService tagService;
    @GetMapping
    public List<? extends TagDTO> listAll( @RequestParam(name = "more", required = false, defaultValue = "false") Boolean more){
        if(more){
            return postTagService.listTagWithCountDtos();
        }
       return tagService.convertTo(tagService.listAll());
    }

    @PostMapping("/createTag")
    public TagDTO createTag(@RequestBody TagParam tagParam){

        Tag tag = tagParam.convertTo();
        return tagService.convertTo(tagService.createTag(tag));
    }
    @PutMapping("/updateTag")
    public TagDTO updateTag(@RequestBody TagParam tagParam){

        Tag tag = tagParam.convertTo();
        return tagService.convertTo(tagService.updateTag(tag));
    }
    @DeleteMapping("/deleteTag/{tagId}")
    public TagDTO deleteTag(@PathVariable("tagId") Integer tagId){

        Tag deletedTag = tagService.removeById(tagId);

        postTagService.removeByTagId(tagId);

        return tagService.convertTo(deletedTag);
    }


}
