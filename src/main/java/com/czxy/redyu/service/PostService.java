package com.czxy.redyu.service;

import com.czxy.redyu.model.dto.*;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.enums.PostStatus;
import com.czxy.redyu.model.params.PostQuery;
import com.czxy.redyu.model.vo.PostDetailVO;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface PostService {

    /**
     * 通过post参数创建文章
     * @param post 文章必须不为空
     * @param tagIds 标签id 的set集合
     * @param categoryIds 分类id的set集合
     *
     */
    PostDetailVO createBy(Post post, Set<Integer> tagIds, Set<Integer> categoryIds);

    PageInfo<? extends BasePostSimpleDTO> listByPage(PostQuery postQuery);

    BasePostSimpleDTO convertToDTO(Post post);

    BasePostMinimalDTO convertToMiniDTO(Post post);

    Post getById(Integer postId);

    PostDetailVO convertToDetailVo(Post post);



    Post updateStatus(PostStatus status, Integer postId);

    /**
     * 查询文章数量
     * @return 返回文章数据
     */
    Integer getPostCount();

    /**
     * 前台文章列表分页
     * @param pageNum 第几页
     * @param pageSize 一页几条
     * @return
     */
    PageInfo<ArchiveSimpleDTO> archiveForPage(Integer pageNum, Integer pageSize);

    ArchiveSimpleDTO convertToArchiveDTO(Post post);

    ArchiveDetailDTO archiveByUrl(String url);

    List<ArchiveSimpleDTO> hotArchive();

    List<ArchiveSimpleDTO> randomArchive();

    Long getAllVisits();


    LocalDateTime getFinalActivity();

    int[] history();

    PageInfo<ArchiveSimpleDTO> archivesByCname(String cname,Integer pageNum);

    SearchResultArchiveDTO searchResult(String content);

    PageInfo<ArchiveSimpleDTO> archivesByTagName(String tagName, Integer pageNum);


    List<ArchivesByYearAndMonthDTO> archivesByYearAndMonth();

    /**
     * 通过文章id删除
     * @param postId 文章id
     */
    void deletePostById(Integer postId);

    PageInfo<ArchiveSimpleDTO> archiveSimpleDTOPageInfo(Integer year, Integer pageNum);


    Integer getVisitsByDays(String days);
}
