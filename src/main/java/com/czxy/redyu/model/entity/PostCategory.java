package com.czxy.redyu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * post_category
 * @author 
 */
@Table(name="post_category")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostCategory implements Serializable {
    /**
     * 主键id
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
     * 分类id
     */
    private Integer categoryId;

    /**
     * 文章id
     */
    private Integer postId;

}