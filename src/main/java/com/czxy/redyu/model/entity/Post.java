package com.czxy.redyu.model.entity;

import com.czxy.redyu.model.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * post
 * @author 
 */
@Table(name="post")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {
    /**
     * 文章主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 格式化后的内容
     */
    private String formatContent;

    /**
     * 原始内容
     */
    private String originalContent;
    /**
     * 状态
     */
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private PostStatus status;

    /**
     * 文章概述
     */
    private String summary;

    /**
     * 标题
     */
    private String title;

    /**
     * 置顶优先级
     */
    private Integer topPriority;

    /**
     * 文章路径
     */
    private String url;

    /**
     * 文章访问量
     */
    private Long visits;

    /**
     * 缩略图
     */
    private String thumbnail;

    private List<Category> categories;

    private List<Tag> tags;

    List<Comment> comments;
}