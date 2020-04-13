package com.czxy.redyu.dao;

import com.czxy.redyu.handler.PostStatusHandler;
import com.czxy.redyu.model.dto.ArchiveSimpleDTO;
import com.czxy.redyu.model.dto.BasePostMinimalDTO;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.params.PostQuery;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface PostMapper extends Mapper<Post> {

    @Select("<script>select * from post" +
            "<where>" +
            "<if test='keyword !=\"\" and keyword != null'> and title like #{keyword} or original_content like #{keyword}</if>" +
        "<if test='status != null'> and status = #{status,typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}</if>" +
        "<if test='postIds != null and postIds.size != 0'> and id in " +
            "<foreach item='item' index='index' collection='postIds' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</if>" +
            "</where> order by create_time desc" +
            "</script>")
    @Results({
            @Result(property = "status",column = "status",typeHandler = PostStatusHandler.class),
            @Result(property = "id",column = "id"),
            @Result(property = "categories",column = "id",many = @Many(select = "com.czxy.redyu.dao.CategoryMapper.selectByPostId")),
            @Result(property = "tags",column = "id",many = @Many(select = "com.czxy.redyu.dao.TagMapper.selectByPostId")),
            @Result(property = "comments",column = "id",many = @Many(select = "com.czxy.redyu.dao.CommentMapper.selectByPostId"))
    })
    List<Post> selectByParam(PostQuery postQuery);

    @Select("select count(*) from post where status=0")
    Integer getPostCount();

    @Select("SELECT * FROM post where status = 0 ORDER BY top_priority desc, create_time DESC ")
    @Results(id = "commentsCount",value =
            @Result(property = "comments",column = "id",many = @Many(select = "com.czxy.redyu.dao.CommentMapper.selectByPostId"))
    )
    List<Post> findAll();

    @Select("select * from post where status = 0 and url=#{url}")
   @Results({
           @Result(property = "id",column = "id"),
           @Result(property = "categories",column = "id",many = @Many(select = "com.czxy.redyu.dao.CategoryMapper.selectByPostId")),
           @Result(property = "tags",column = "id",many = @Many(select = "com.czxy.redyu.dao.TagMapper.selectByPostId")),
           @Result(property = "comments",column = "id",many = @Many(select = "com.czxy.redyu.dao.CommentMapper.selectByPostId"))
   })
    Post archiveByUrl(String url);


    @Select("SELECT * FROM post where status = 0 ORDER BY visits DESC  LIMIT 5")
    List<ArchiveSimpleDTO> visitsDesc();

    @Select("SELECT SUM(visits) FROM post where status = 0")
    Long selectSumVisits();

    @Select("SELECT update_time FROM post ORDER BY update_time DESC LIMIT 1")
    LocalDateTime getFinalUpdatePost();

    @Select("SELECT DISTINCT DATE_FORMAT(create_time,'%Y') FROM post where status = 0")
    int[] findHistory();

    @Select("SELECT p.* FROM post p,post_category pc  WHERE  p.id=pc.post_id and p.status =0  AND pc.category_id=(SELECT id FROM category WHERE slug_name = #{cname}) order by create_time desc")
    @ResultMap("commentsCount")
    List<Post> findPostByCname(String cname);


    @Select("select * from post where status =0 and original_content like #{content}")
    List<Post> searchResultByKeyword(String content);

    /**
     *like ____content____
     * @param content
     * @return
     */
    @Select("select * from post where status =0 and original_content like #{content}")
    List<Post> searchResultByKeywordForLike(String content);

    @Select("select url from post where id = #{id}")
    String selectUrlById(Integer id);

    @Select("SELECT p.* FROM post p,post_tag pt  WHERE p.id=pt.post_id and p.status =0 AND  pt.tag_id=(SELECT id FROM tag WHERE slug_name = #{tagName}) order by create_time desc")
    @ResultMap("commentsCount")
    List<Post> findPostByTagName(String tagName);


    @Select("select date_format(create_time, '%Y%m') as yearMonth  from post where status = 0  group by  yearMonth ORDER BY yearMonth DESC")
    List<Integer> findAllYearAndMonth();

    @Select("SELECT * FROM post WHERE status = 0 and DATE_FORMAT(create_time, '%Y%m') = #{yearMonth} ORDER BY create_time desc")
    List<Post> findPostByYearAndMonth(Integer yearMonth);

    @Select("select * from post where status=0 and year(create_time)=#{year}")
    @ResultMap("commentsCount")
    List<Post> findPostByYear(Integer year);

    @Select("SELECT IFNULL(SUM(visits),0) FROM post WHERE DATE_FORMAT(create_time,'%Y-%m-%d') = #{day}")
    Integer getVisitsByDays(@Param("day") String days);

    @Select("SELECT url,title FROM post WHERE id = (SELECT MIN(id) FROM post WHERE STATUS=0 AND id > #{id}) OR id = (SELECT MAX(id) FROM post WHERE STATUS=0 AND id < #{id}) order by create_time desc")
    List<BasePostMinimalDTO> selectLastPostAndNextPost(Integer id);

    @Update("UPDATE post SET visits = #{visits} WHERE id = #{id}")
    void updateVisitById(@Param("id") Integer id, @Param("visits") Long visits);
}