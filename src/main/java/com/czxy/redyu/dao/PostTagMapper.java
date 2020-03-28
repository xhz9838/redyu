package com.czxy.redyu.dao;

import com.czxy.redyu.model.entity.PostTag;
import com.czxy.redyu.model.projection.TagPostPostCountProjection;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface PostTagMapper extends Mapper<PostTag> {

    @Select("SELECT COUNT(pt.post_id) AS post_count, pt.tag_id FROM post_tag pt GROUP BY pt.tag_id")
    List<TagPostPostCountProjection> findPostCount();
}