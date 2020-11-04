package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.PostMapper;
import com.czxy.redyu.exception.BadRequestException;
import com.czxy.redyu.model.dto.*;
import com.czxy.redyu.model.entity.*;
import com.czxy.redyu.model.enums.PostStatus;
import com.czxy.redyu.model.params.PostQuery;
import com.czxy.redyu.model.vo.PostDetailVO;
import com.czxy.redyu.service.*;
import com.czxy.redyu.utils.MarkdownUtils;
import com.czxy.redyu.utils.ServiceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {
    @Resource
    private PostTagService postTagService;
    @Resource
    private PostCategoryService postCategoryService;
    @Resource
    private PostMapper postMapper;
    @Resource
    private TagService tagService;
    @Resource
    private CategoryService categoryService;

    @Value("${redyu.desk.url}")
    private String deskUrl;

    @Override
    public PostDetailVO createBy(Post postToCreate, Set<Integer> tagIds, Set<Integer> categoryIds) {

        return createOrUpdate(postToCreate, tagIds, categoryIds);
    }

    @Override
    public PageInfo<? extends BasePostSimpleDTO> listByPage(PostQuery postQuery) {

        PageHelper.startPage(postQuery.getPageNum(), postQuery.getPageSize());
        List<Integer> postIds = null;
        if (postQuery.getCategoryId() != null && postQuery.getCategoryId() != 0) {
            postIds = new ArrayList<>();
            List<PostCategory> postCategories = postCategoryService.findPCSByCid(postQuery.getCategoryId());
            postIds = postCategories.stream().map(postCategory -> postCategory.getPostId()).collect(Collectors.toList());
            postQuery.setPostIds(postIds);
        }

        if (postQuery.getKeyword() != null && !"".equals(postQuery.getKeyword())) {
            postQuery.setKeyword("%" + postQuery.getKeyword() + "%");
        }

        List<Post> posts = new ArrayList<>();
        if (postIds == null || postIds.size() != 0) {
            posts = postMapper.selectByParam(postQuery);
        }
        List<BasePostSimpleDTO> basePostSimpleDTOS = posts.stream().map(this::convertToDTO).collect(Collectors.toList());
        PageInfo<Post> postPageInfo = new PageInfo<>(posts);

        PageInfo<BasePostSimpleDTO> basePostSimpleDTOPageInfo = new PageInfo<>();

        BeanUtils.copyProperties(postPageInfo, basePostSimpleDTOPageInfo);
        basePostSimpleDTOPageInfo.setList(basePostSimpleDTOS);
        return basePostSimpleDTOPageInfo;
    }


    @Override
    public Post getById(Integer postId) {
        return postMapper.selectByPrimaryKey(postId);
    }

    @Override
    public PostDetailVO convertToDetailVo(Post post) {
        List<Tag> tags = postTagService.listTagsBy(post.getId());
        List<Category> categories = postCategoryService.listCategoriesBy(post.getId());

        return convertTo(post, tags, categories);
    }

    @Override
    public Post updateStatus(PostStatus status, Integer postId) {
        Post post = new Post();
        post.setId(postId);
        post.setStatus(status);
        postMapper.updateByPrimaryKeySelective(post);
        return postMapper.selectByPrimaryKey(postId);
    }

    @Override
    public Integer getPostCount() {
        return postMapper.getPostCount();
    }

    @Override
    public PageInfo<ArchiveSimpleDTO> archiveForPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postMapper.findAll();
        List<ArchiveSimpleDTO> archiveSimpleDTOS = posts.stream().map(this::convertToArchiveDTO).collect(Collectors.toList());

        PageInfo<Post> postPageInfo = new PageInfo<>(posts);
        PageInfo<ArchiveSimpleDTO> archiveSimpleDTOPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(postPageInfo, archiveSimpleDTOPageInfo);
        archiveSimpleDTOPageInfo.setList(archiveSimpleDTOS);
        return archiveSimpleDTOPageInfo;
    }

    @Override
    public PageInfo<ArchiveSimpleDTO> archivesByCname(String cname, Integer pageNum) {
        PageHelper.startPage(pageNum, 5);

        List<Post> posts = postMapper.findPostByCname(cname);
        List<ArchiveSimpleDTO> archiveSimpleDTOS = posts.stream().map(this::convertToArchiveDTO).collect(Collectors.toList());

        PageInfo<Post> postPageInfo = new PageInfo<>(posts);
        PageInfo<ArchiveSimpleDTO> archiveSimpleDTOPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(postPageInfo, archiveSimpleDTOPageInfo);
        archiveSimpleDTOPageInfo.setList(archiveSimpleDTOS);
        return archiveSimpleDTOPageInfo;
    }

    @Override
    public SearchResultArchiveDTO searchResult(final String content) {
        Assert.notNull(content, "搜索内容不能为null");
        String searchLikeContent = content;
        if (!"".equals(content)) {
            searchLikeContent = "%" + content + "%";
        }
        List<Post> posts = postMapper.searchResultByKeyword(searchLikeContent);

        StringBuffer buffer = new StringBuffer();
        posts.stream().forEach(post -> {
            int startIndex;
            int endIndex;
            int contentIndex = post.getOriginalContent().toLowerCase().indexOf(content.toLowerCase());
            if (contentIndex < 5) {
                startIndex = 0;
            } else {
                startIndex = contentIndex - 5;
            }
            if (post.getOriginalContent().length() - contentIndex < 5) {
                endIndex = post.getOriginalContent().length();
            } else {
                endIndex = contentIndex + 5;
            }

            String beforeContent = post.getOriginalContent()
                    .substring(startIndex, contentIndex);
            String afterContent = post.getOriginalContent()
                    .substring(contentIndex+content.length(), endIndex);
            buffer.append("<li><a href='")
                    .append(deskUrl)
                    .append("/archives/")
                    .append(post.getUrl())
                    .append("'>")
                    .append(post.getTitle())
                    .append("<p class='text-muted'>")
                    .append(beforeContent)
                    .append("<mark class='text_match'>")
                    .append(content)
                    .append("</mark>")
                    .append(afterContent)
                    .append("</p></a></li>");

        });
        SearchResultArchiveDTO searchResultArchiveDTO = new SearchResultArchiveDTO();
        searchResultArchiveDTO.setResults(buffer.toString());
        return searchResultArchiveDTO;
    }

    @Override
    public PageInfo<ArchiveSimpleDTO> archivesByTagName(String tagName, Integer pageNum) {
        PageHelper.startPage(pageNum, 5);
        List<Post> posts = postMapper.findPostByTagName(tagName);
        List<ArchiveSimpleDTO> archiveSimpleDTOS = posts.stream().map(this::convertToArchiveDTO).collect(Collectors.toList());

        PageInfo<Post> postPageInfo = new PageInfo<>(posts);
        PageInfo<ArchiveSimpleDTO> archiveSimpleDTOPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(postPageInfo, archiveSimpleDTOPageInfo);
        archiveSimpleDTOPageInfo.setList(archiveSimpleDTOS);
        return archiveSimpleDTOPageInfo;
    }

    @Override
    public List<ArchivesByYearAndMonthDTO> archivesByYearAndMonth() {
        List<Integer> allYearAndMonth = postMapper.findAllYearAndMonth();
        List<ArchivesByYearAndMonthDTO> archivesByYearAndMonthDTOS = new ArrayList<>();
        allYearAndMonth.forEach(yearMonth ->{
            List<Post> posts = postMapper.findPostByYearAndMonth(yearMonth);
            List<BasePostMinimalDTO> basePostMinimalDTOS = posts.stream().map(this::convertToMiniDTO).collect(Collectors.toList());

            ArchivesByYearAndMonthDTO archivesByYearAndMonthDTO = new ArchivesByYearAndMonthDTO();
            archivesByYearAndMonthDTO.setBasePostMinimalDTOList(basePostMinimalDTOS);
            archivesByYearAndMonthDTOS.add(archivesByYearAndMonthDTO);
        });
        return archivesByYearAndMonthDTOS;
    }

    @Override
    public void deletePostById(Integer postId) {
        postMapper.deleteByPrimaryKey(postId);
    }

    @Override
    public PageInfo<ArchiveSimpleDTO> archiveSimpleDTOPageInfo(Integer year, Integer pageNum) {
        PageHelper.startPage(pageNum,5);

        List<Post> posts = postMapper.findPostByYear(year);
        PageInfo<Post> postPageInfo = new PageInfo<>(posts);
        List<ArchiveSimpleDTO> archiveSimpleDTOS = posts.stream().map(this::convertToArchiveDTO).collect(Collectors.toList());
        PageInfo<ArchiveSimpleDTO> archiveSimpleDTOPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(postPageInfo,archiveSimpleDTOPageInfo);
        archiveSimpleDTOPageInfo.setList(archiveSimpleDTOS);
        return archiveSimpleDTOPageInfo;
    }

    @Override
    public Integer getVisitsByDays(String days) {
        return postMapper.getVisitsByDays(days);
    }

    @Override
    public ArchiveDetailDTO   archiveByUrl(String url) {
        Post post = postMapper.archiveByUrl(url);
        if (post == null) {
            throw new BadRequestException("文章不存在");
        }
        Map<String, List<String>> frontMatter = MarkdownUtils.getFrontMatter(post.getOriginalContent());

        System.out.println(frontMatter);
        List<BasePostMinimalDTO> nextAndLast = postMapper.selectLastPostAndNextPost(post.getId());
        post.setVisits(post.getVisits() + 1);
        postMapper.updateByPrimaryKey(post);
        ArchiveDetailDTO archiveDetailDTO = new ArchiveDetailDTO().convertFrom(post);
       archiveDetailDTO.setNextAndLast(nextAndLast);

        return archiveDetailDTO;

    }

    @Override
    public List<ArchiveSimpleDTO> hotArchive() {
        return postMapper.visitsDesc();
    }

    @Override
    public List<ArchiveSimpleDTO> randomArchive() {

        List<Post> postList = postMapper.findAll();
        List<Post> newPosts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            newPosts.add(postList.get(new Random().nextInt(postList.size())));
        }
        return newPosts.stream().map(this::convertToArchiveDTO).collect(Collectors.toList());
    }

    @Override
    public Long getAllVisits() {
        return postMapper.selectSumVisits();
    }

    @Override
    public LocalDateTime getFinalActivity() {
        return postMapper.getFinalUpdatePost();

    }

    @Override
    public int[] history() {
        return postMapper.findHistory();

    }


    @Override
    public BasePostSimpleDTO convertToDTO(Post post) {

        return new BasePostSimpleDTO().convertFrom(post);
    }

    @Override
    public BasePostMinimalDTO convertToMiniDTO(Post post) {

        return new BasePostMinimalDTO().convertFrom(post);
    }

    @Override
    public ArchiveSimpleDTO convertToArchiveDTO(Post post) {
        return new ArchiveSimpleDTO().convertFrom(post);
    }


    private PostDetailVO convertTo(@org.springframework.lang.NonNull Post post, @Nullable List<Tag> tags, @Nullable List<Category> categories) {
        Assert.notNull(post, "Post must not be null");

        PostDetailVO postDetailVO = new PostDetailVO().convertFrom(post);

        if (StringUtils.isBlank(postDetailVO.getSummary())) {
            postDetailVO.setSummary(post.getFormatContent());
        }

        Set<Integer> tagIds = ServiceUtils.fetchProperty(tags, Tag::getId);
        Set<Integer> categoryIds = ServiceUtils.fetchProperty(categories, Category::getId);


        postDetailVO.setTagIds(tagIds);
        postDetailVO.setTags(tagService.convertTo(tags));

        postDetailVO.setCategoryIds(categoryIds);
        postDetailVO.setCategories(categoryService.convertTo(categories));

        return postDetailVO;
    }

    private PostDetailVO createOrUpdate(@NonNull Post post, Set<Integer> tagIds, Set<Integer> categoryIds) {
        Assert.notNull(post, "Post参数不能为null");
        PostStatus status = post.getStatus();
        // 创建或修改
        post = createOrUpdateBy(post);

        postTagService.removeByPostId(post.getId());

        postCategoryService.removeByPostId(post.getId());

        List<Tag> tags = tagService.listAllByIds(tagIds);

        List<Category> categories = categoryService.listAllByIds(categoryIds);

        List<PostTag> postTags = postTagService.mergeOrCreateByIfAbsent(post.getId(), ServiceUtils.fetchProperty(tags, Tag::getId));

        List<PostCategory> postCategories = postCategoryService.mergeOrCreateByIfAbsent(post.getId(), ServiceUtils.fetchProperty(categories, Category::getId));

        return new PostDetailVO().convertFrom(post);
    }

    private Post createOrUpdateBy(Post post) {
        // 判断文章状态，
        //发布状态
        if (post.getStatus() == PostStatus.PUBLISHED) {
            post.setFormatContent(MarkdownUtils.renderHtml(post.getOriginalContent()));
        }
        // 创建或修改文章
        if (ServiceUtils.isEmptyId(post.getId())) {
            // 将创建文章
            return create(post);
        }
        return update(post);

    }

    private Post update(Post post) {

        postMapper.updateByPrimaryKey(post);
        return post;
    }

    private Post create(Post post) {
        post.setVisits(0L);
        postMapper.insert(post);
        return post;
    }

}
