package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.PostMapper;
import com.czxy.redyu.exception.BadRequestException;
import com.czxy.redyu.model.dto.ArchiveDetailDTO;
import com.czxy.redyu.model.dto.BasePostMinimalDTO;
import com.czxy.redyu.model.dto.SearchResultArchiveDTO;
import com.czxy.redyu.model.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class PostServiceImplTest {
    @Resource
    private PostMapper postMapper;

    @Test
    public void getAllVisits() {
        Long aLong = postMapper.selectSumVisits();

        System.out.println(aLong);
    }


    @Test
    public void getFinalActivity() {
        LocalDateTime finalUpdatePost = postMapper.getFinalUpdatePost();
        System.out.println(finalUpdatePost);
    }

    @Test
    public void history() {
        int[] history = postMapper.findHistory();
        System.out.println(Arrays.toString(history));
    }

    @Test
    public void searchResult() {
        String content = "![img]";
        String searchLikeContent = content;
        if (!"".equals(content)) {
            searchLikeContent = "%" + content + "%";
        }
        List<Post> posts = postMapper.searchResultByKeyword(searchLikeContent);

        StringBuffer buffer = new StringBuffer();
        posts.stream().forEach(post -> {
            int startIndex;
            int endIndex;
            int contentIndex = post.getOriginalContent().indexOf(content);
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
                    .substring(contentIndex, endIndex);
            buffer.append("<li><a href='")
                    .append("http://loaclhost:3000")
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
        System.out.println(searchResultArchiveDTO);
    }

    @Test
    public void findAllYearAndMonth(){
        List<Integer> allYearAndMonth = postMapper.findAllYearAndMonth();
        System.out.println(allYearAndMonth);
    }
    @Test
    public void findPostByYearAndMonth(){
        List<Post> posts = postMapper.findPostByYearAndMonth(202003);
        System.out.println(posts);
    }

    @Test
    public void archiveByUrl() {
        Post post = postMapper.archiveByUrl("1584264741353");
        if (post == null) {
            throw new BadRequestException("文章不存在");
        }

        List<BasePostMinimalDTO> nextAndLast = postMapper.selectLastPostAndNextPost(post.getId());
        post.setVisits(post.getVisits() + 1);
        postMapper.updateByPrimaryKey(post);
        ArchiveDetailDTO archiveDetailDTO = new ArchiveDetailDTO().convertFrom(post);


        System.out.println(archiveDetailDTO);
    }
}