package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.PostMapper;
import com.czxy.redyu.model.dto.SearchResultArchiveDTO;
import com.czxy.redyu.model.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/4/3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PostServiceImplTest {

    @Resource
    private PostMapper postMapper;
    @Value("${redyu.desk.url}")
    private String deskUrl;

    @Test
    public void searchResult() {
        String content = "j";
        String searchLikeContent = content;

            searchLikeContent = "%" + content + "%";

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
            System.out.println(post.getTitle());
            System.out.println(startIndex);
            System.out.println(contentIndex);
            System.out.println(endIndex);
            System.out.println("-----");
            String beforeContent = post.getOriginalContent()
                    .substring(startIndex, contentIndex);
            String afterContent = post.getOriginalContent()
                    .substring(contentIndex, endIndex);
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
        System.out.println(searchResultArchiveDTO.getResults());
    }
}