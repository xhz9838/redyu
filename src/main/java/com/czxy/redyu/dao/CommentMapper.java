package com.czxy.redyu.dao;

import com.czxy.redyu.model.entity.Comment;
import com.czxy.redyu.model.params.CommentQuery;
import com.czxy.redyu.model.projection.CommentChildrenCountProjection;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

@org.apache.ibatis.annotations.Mapper
public interface CommentMapper extends Mapper<Comment> {
    /**
     * 根据文章id查询所有评论
     * @param postId 文章id
     * @return 所有评论
     */
    @Select("SELECT * FROM comment WHERE post_id=#{postId}")
    List<Comment> selectByPostId(@Param("postId") Integer postId);

    /**
     * 查询所有评论数
     * @return 评论总数
     */
    @Select("select count(*) from comment")
    Integer getCommentCount();

    /**
     * 根据文章id查询评论列表
     * @param postId 文章id
     * @return 评论列表
     */
    @Select("select * from comment where post_id=#{postId} and status = 1 order by create_time desc")
    List<Comment> listCommentByPostId(Integer postId);

    /**
     * 根据父id列表查询评论
     * @param parentIds 父id列表
     * @return 评论列表
     */
    @Select("<script>" +
            "select * from comment where id in" +
            " <foreach item='item' collection='parentIds' open='(' separator=',' close= ')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
     List<Comment> listCommentById(@Param("parentIds") Set<Long> parentIds);

    /**
     * 根据文章id按评论时间查询发布状态的文章
     * @param postId
     * @return
     */
    @Select("select * from comment where post_id=#{postId} and status = 1 and parent_id = 0 order by create_time desc")
    List<Comment> pageVosByPage(Integer postId);

    /**
     * 根据父id查询发布评论的列表
     * @param id 父id
     * @return 评论列表
     */
    @Select("select * from comment where parent_id=#{id} and status = 1 order by create_time desc")
    List<Comment> findChildCommentById(Long id);

    /**
     * 根据查询条件动态查询评论列表
     * @param commentQuery 查询条件
     * @return 评论列表
     */
    @Select("<script>select * from comment" +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'> and content like #{keyword} or author like #{keyword} or email like #{keyword}</if>" +
            "<if test=' status != null'> and status = #{status}</if>" +
            "</where> order by create_time desc" +
            "</script>")
    @Results({
            @Result(property = "postId",column = "post_id"),
            @Result(property = "post",column = "post_id",one = @One(select = "com.czxy.redyu.dao.PostMapper.selectByPrimaryKey"))
    })
    List<Comment> findAllCommentByParams(CommentQuery commentQuery);

    /**
     * 通过评论id 修改状态
     * @param id 评论id
     * @param status 需要修改的状态
     */
    @Update("update comment set status = #{status} where id= #{id}")
    void updateStatusByCid(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 通过所在文章id 评论父id 和状态 查询所有评论
     * @param targetId 文章id
     * @param status 状态
     * @param l 评论父id
     * @param <COMMENT> 评论
     * @return 评论列表
     */
    @Select("select * from comment where post_id=#{postId} and status=#{status} and parent_id=#{parentId} order by create_time desc")
            @Results(id = "getAuthorById",value = {
                    @Result(property = "parentId",column = "parent_id"),
                    @Result(property = "parentAuthor",column = "parent_id",one = @One(select = "com.czxy.redyu.dao.CommentMapper.getAuthorById"))
            })
    <COMMENT extends Comment> List<COMMENT> findAllByPostIdAndStatusAndParentId(@Param("postId") Integer targetId, @Param("status") Integer status,@Param("parentId") long l);

    /**
     * 通过一级评论id查询二级评论列表
     * @param topCommentIds
     * @return
     */
    @Select("<script>SELECT COUNT(id) AS directChildrenCount,parent_id AS commentId FROM `comment` WHERE  parent_id IN  " +
            "  <foreach collection=\"topCommentIds\" open=\"(\" separator=\",\" close=\")\" item=\"id\">\n" +
            "      #{id}\n" +
            "    </foreach>" +
            "GROUP BY parent_id</script>")
    List<CommentChildrenCountProjection> findDirectChildrenCount(@Param("topCommentIds") Set<Long> topCommentIds);

    /**
     * 通过评论父id列表查询状态发布的评论
     * @param status 评论状态
     * @param commentIds 评论父id列表
     * @param <COMMENT> 评论
     * @return 评论列表
     */
    @Select("<script>select * from comment where status = #{status} and parent_id in" +
            "  <foreach collection=\"commentIds\" open=\"(\" separator=\",\" close=\")\" item=\"id\">\n" +
            "      #{id}\n" +
            "    </foreach>" +
            "</script>")
            @ResultMap("getAuthorById")
    <COMMENT extends Comment> List<COMMENT> findAllByStatusAndParentIdIn(@Param("status") Integer status, @Param("commentIds") Set<Long> commentIds);

    /**
     * 通过评论id查询作者昵称
     * @param id 评论id
     * @return 作者昵称
     */
    @Select("select author from comment where id = #{id}")
    String getAuthorById(Integer id);

    /**
     * 查询最新5条评论 将文章url查出来
     * @return 评论列表
     */
    @Select("SELECT * FROM `comment` ORDER BY create_time DESC LIMIT 5")
    @Results({
            @Result(property = "postId",column = "post_id"),
            @Result(property = "postUrl",column = "post_id",one = @One(select = "com.czxy.redyu.dao.PostMapper.selectUrlById"))
    })
    List<Comment> hotComment();
}