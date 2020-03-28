package com.czxy.redyu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * comment
 * @author 
 */

/*text: 3242
author: 432
mail: 15387066867@163.com
receiveMail: yes
url:
comment_post_ID:
comment_parent:*/
@Table(name="comment")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    /**
     * 评论id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 评论者名
     */
    private String author;

    /**
     * 评论者地址
     */
    private String authorUrl;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者邮箱
     */
    private String email;

    /**
     * 评论头像
     */
    private String gravatarMd5;

    /**
     * 评论ip地址
     */
    private String ipAddress;

    /**
     * 是否是管理员
     */
    private Byte isAdmin;

    /**
     * 评论的上一级id
     */
    private Long parentId =0L;

    /**
     * 评论的文章id
     */
    private Integer postId;

    /**
     * 评论状态
     * 0 回收站
     * 1 发布
     */
    private Integer status;

    /**
     * 置顶
     * 0不置顶
     * 1置顶
     */
    private Integer topPriority;

    /**
     * 评论用户代理
     */
    private String userAgent;


    @Transient
    private String parentAuthor;

    @Transient
    private String postUrl;

    private Post post;
}