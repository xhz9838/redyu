package com.czxy.redyu.dao;

import com.czxy.redyu.model.entity.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface CategoryMapper extends Mapper<Category> {

    @Select("select * from category where parent_id =#{parentId}")
    List<Category> findParent(@Param("parentId") Integer parentId);

    @Select("SELECT c.* FROM category c,post_category pc WHERE c.id=pc.category_id AND pc.post_id=#{postId}")
    List<Category> selectByPostId(@Param("postId") Integer postId);
}