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
 * post_tag
 * @author 
 */
@Table(name="post_tag")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostTag implements Serializable {
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
     * 文章id
     */
    private Integer postId;

    /**
     * 标签id
     */
    private Integer tagId;

}