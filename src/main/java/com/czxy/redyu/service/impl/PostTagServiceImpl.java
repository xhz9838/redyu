package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.PostTagMapper;
import com.czxy.redyu.dao.TagMapper;
import com.czxy.redyu.model.dto.TagWithPostCountDTO;
import com.czxy.redyu.model.entity.PostTag;
import com.czxy.redyu.model.entity.Tag;
import com.czxy.redyu.model.projection.TagPostPostCountProjection;
import com.czxy.redyu.service.PostTagService;
import com.czxy.redyu.utils.ServiceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
public class PostTagServiceImpl implements PostTagService {
    @Resource
    private PostTagMapper postTagMapper;
    @Resource
    private TagMapper tagMapper;
    @Override
    public void removeByPostId(Integer id) {
        Example example = new Example(PostTag.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("postId",id);
        postTagMapper.deleteByExample(example);
    }

    @Override
    public List<PostTag> mergeOrCreateByIfAbsent(Integer postId, Set<Integer> tagIds) {
        List<PostTag> postTagsStaging = tagIds.stream().map(tagId -> {

            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postTagMapper.insert(postTag);
            return postTag;
        }).collect(Collectors.toList());

        return postTagsStaging;
    }

    @Override
    public List<TagWithPostCountDTO> listTagWithCountDtos() {

        List<Tag> tags = tagMapper.selectAll();

        Map<Integer, Long> tagPostCountMap = ServiceUtils.convertToMap(postTagMapper.findPostCount(), TagPostPostCountProjection::getTagId, TagPostPostCountProjection::getPostCount);

        return tags.stream().map(
                tag -> {
                    TagWithPostCountDTO tagWithCountOutputDTO = new TagWithPostCountDTO().convertFrom(tag);
                    tagWithCountOutputDTO.setPostCount(tagPostCountMap.getOrDefault(tag.getId(), 0L));
                    return tagWithCountOutputDTO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void removeByTagId(Integer tagId) {
        Assert.notNull(tagId, "标签id必须不为空");
        Example example = new Example(PostTag.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("tagId",tagId);
        postTagMapper.deleteByExample(example);
    }

    @Override
    public List<Tag> listTagsBy(Integer id) {
        Example example = new Example(PostTag.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("postId",id);
        List<PostTag> postTags = postTagMapper.selectByExample(example);
        List<Tag> tagList =new ArrayList<>();
        for (PostTag postTag : postTags) {
            Tag tag = tagMapper.selectByPrimaryKey(postTag.getTagId());
            tagList.add(tag);
        }
        return tagList;
    }
}
