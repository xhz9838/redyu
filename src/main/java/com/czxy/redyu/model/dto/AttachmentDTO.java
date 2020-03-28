package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.dto.base.OutputConverter;
import com.czxy.redyu.model.entity.Attachment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/15
 */
@Data
@ToString
public class AttachmentDTO implements OutputConverter<AttachmentDTO,Attachment> {
    /**
     * 附件唯一名称
     */
    private String fileKey;

    private String path;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

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
