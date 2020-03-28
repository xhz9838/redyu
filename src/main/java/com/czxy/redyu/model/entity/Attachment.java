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
 * attachment
 * @author xuhongzu
 */
@Table(name="attachment")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Attachment implements Serializable {
    /**
     * 附件主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 附件创建时间
     */
    private LocalDateTime createTime;

    /**
     * 附件修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 附件名称
     */
    private String fileKey;

    /**
     * 附件长度(像素)
     */
    private Integer height;

    /**
     * 附件宽度（像素）
     */
    private Integer width;

    /**
     * 附件媒体类型
     */
    private String mediaType;

    /**
     * 附件名
     */
    private String name;

    /**
     * 附件路径
     */
    private String path;

    /**
     * 附件大小
     */
    private Long size;

    /**
     * 附件扩展名
     */
    private String suffix;

    /**
     * 附件缩略图
     */
    private String thumbPath;

    /**
     * 附件储存地 0：本地  4：阿里云...
     */
    private Integer type;
}