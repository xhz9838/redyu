package com.czxy.redyu.dao;

import com.czxy.redyu.model.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TagMapper extends Mapper<Tag> {


    @Select("SELECT t.* FROM tag t,post_tag pt WHERE t.id=pt.tag_id AND pt.post_id=#{postId}")
    List<Tag> selectByPostId(@Param("postId") Integer postId);
}