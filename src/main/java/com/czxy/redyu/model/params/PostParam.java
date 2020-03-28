package com.czxy.redyu.model.params;

import com.czxy.redyu.model.dto.base.InputConverter;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.enums.PostStatus;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */
@Data
public class PostParam implements InputConverter<Post> {

    private Integer id;

    @NotBlank(message = "文章标题不能为空")
    @Size(max = 100, message = "文章标题的字符长度不能超过 {max}")
    private String title;

    private PostStatus status = PostStatus.DRAFT;

    private String url;

    @NotBlank(message = "文章内容不能为空")
    private String originalContent;
    /**
     * 格式化后的内容
     */
    private String formatContent;

    @Size(max = 1024 , message = "文章摘要不能超过1024")
    private String summary;

    @Size(max = 255, message = "文章缩略图长度不能超过{max}")
    private String thumbnail;

    @Min(value = 0, message = "文章置顶优先级不能小于{value}")
    private Integer topPriority = 0;

    /**
     * 文章访问量
     */
    private Long visits;

   private Set<Integer> tagIds;

   private Set<Integer> categoryIds;

    @Override
    public Post convertTo() {
        if(StringUtils.isBlank(url)){
            url = title.replace(".","");
        }
        if(null == thumbnail){
            thumbnail = "";
        }
        return InputConverter.super.convertTo();
    }

    @Override
    public void update(Post post) {
        if(StringUtils.isBlank(url)){
            url = title.replace(".","");
        }
        if(null == thumbnail){
            thumbnail = "";
        }
        InputConverter.super.update(post);
    }
}
