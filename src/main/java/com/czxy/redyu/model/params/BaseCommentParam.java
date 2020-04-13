package com.czxy.redyu.model.params;

import com.czxy.redyu.model.dto.base.InputConverter;
import com.czxy.redyu.utils.ReflectionUtils;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.reflect.ParameterizedType;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Data
public abstract class BaseCommentParam<COMMENT> implements InputConverter<COMMENT> {

    @NotBlank(message = "评论者昵称不能为空")
    @Size(max = 50, message = "评论者昵称的字符长度不能超过 {max}")
    private String author;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 255, message = "邮箱的字符长度不能超过 {max}")
    private String email;

    @Size(max = 127, message = "评论者博客链接的字符长度不能超过 {max}")
    @URL(message = "博客链接格式不正确")
    private String authorUrl;

    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1023, message = "评论内容的字符长度不能超过 {max}")
    private String content;

    @Min(value = 1, message = "Post id must not be less than {value}")
    private Integer postId;

    @Min(value = 0, message = "PostComment parent id must not be less than {value}")
    private Long parentId = 0L;

    private Boolean allowNotification = true;

    @Override
    public ParameterizedType parameterizedType() {
        return ReflectionUtils.getParameterizedTypeBySuperClass(BaseCommentParam.class, this.getClass());
    }
}
