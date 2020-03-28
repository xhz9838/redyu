package com.czxy.redyu.service;

import com.czxy.redyu.model.dto.TagWithPostCountDTO;
import com.czxy.redyu.model.entity.PostTag;
import com.czxy.redyu.model.entity.Tag;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface PostTagService {
    void removeByPostId(Integer id);

    List<PostTag> mergeOrCreateByIfAbsent(@NonNull Integer postId, @Nullable Set<Integer> tagIds);

    List<TagWithPostCountDTO> listTagWithCountDtos();

    void removeByTagId(Integer tagId);

    List<Tag> listTagsBy(Integer id);
}
