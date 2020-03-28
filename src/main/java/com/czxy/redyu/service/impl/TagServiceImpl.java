package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.TagMapper;
import com.czxy.redyu.model.dto.TagDTO;
import com.czxy.redyu.model.entity.Tag;
import com.czxy.redyu.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;
    @Override
    public List<Tag> listAll() {
      return  tagMapper.selectAll();
    }


    @Override
    public TagDTO convertTo(Tag tag) {
        Assert.notNull(tag, "标签必须不为空");

        return new TagDTO().convertFrom(tag);
    }
    @Override
    public List<TagDTO> convertTo(List<Tag> tags) {
        if(CollectionUtils.isEmpty(tags)){
            return Collections.emptyList();
        }
        return tags.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }

    @Override
    public Tag createTag(Tag tag) {
        tagMapper.insert(tag);

        return tagMapper.selectByPrimaryKey(tag.getId());
    }

    @Override
    public List<Tag> listAllByIds(Set<Integer> tagIds) {

        List<Tag> tags = new ArrayList<>();
        for (Integer tagId : tagIds) {

            tags.add(tagMapper.selectByPrimaryKey(tagId));
        }
        return tags;
    }

    @Override
    public Tag updateTag(Tag tag) {
        Assert.notNull(tag, "标签必须不为空");
        tagMapper.updateByPrimaryKey(tag);
        return tagMapper.selectByPrimaryKey(tag.getId());
    }

    @Override
    public Tag removeById(Integer tagId) {
        Assert.notNull(tagId, "标签Id必须不为空");
        Tag tag = tagMapper.selectByPrimaryKey(tagId);
        tagMapper.deleteByPrimaryKey(tagId);
        return tag;
    }

    @Override
    public List<TagDTO> tagList() {
        List<Tag> tags = tagMapper.selectAll();
        return tags.stream().map(this::convertTo).collect(Collectors.toList());
    }
}
