package com.czxy.redyu.dao;

import com.czxy.redyu.model.entity.PostCategory;
import com.czxy.redyu.model.projection.CategoryPostCountProjection;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface PostCategoryMapper extends Mapper<PostCategory> {

    @Select("SELECT COUNT(pc.post_id) as post_count, pc.category_id FROM post_category pc GROUP BY pc.category_id")
    List<CategoryPostCountProjection> findPostCount();

}