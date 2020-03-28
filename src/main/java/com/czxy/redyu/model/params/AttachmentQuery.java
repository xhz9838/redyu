package com.czxy.redyu.model.params;

import com.czxy.redyu.model.dto.base.InputConverter;
import com.czxy.redyu.model.entity.Attachment;
import lombok.Data;
import lombok.ToString;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/15
 */
@Data
@ToString
public class AttachmentQuery implements InputConverter<Attachment> {


    private Integer pageSize;

    private Integer pageNum;

    private String name;

    private String suffix;

    private Integer type;



}
