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
 * category
 * @author xuhongzu
 */
@Table(name="category")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
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
     * 描述
     */
    private String description;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 别称
     */
    private String slugName;
}