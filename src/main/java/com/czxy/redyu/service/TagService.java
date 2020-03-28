package com.czxy.redyu.service;

import com.czxy.redyu.model.dto.TagDTO;
import com.czxy.redyu.model.entity.Tag;

import java.util.List;
import java.util.Set;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface TagService {
    List<Tag> listAll();

    public TagDTO convertTo(Tag tag);

    List<TagDTO> convertTo(List<Tag> tags);

    Tag createTag(Tag tag);

    List<Tag> listAllByIds(Set<Integer> tagIds);

    Tag updateTag(Tag tag);

    Tag removeById(Integer tagId);

    List<TagDTO> tagList();
}
