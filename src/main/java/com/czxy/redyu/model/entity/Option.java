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
 * option
 * @author 
 */
@Table(name="`option`")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Option implements Serializable {
    /**
     * 选项id
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
     * 选项键
     */
    private String optionKey;

    /**
     * 值
     */
    private String optionValue;

}