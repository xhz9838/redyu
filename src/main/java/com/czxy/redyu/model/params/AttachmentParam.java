package com.czxy.redyu.model.params;

import com.czxy.redyu.model.dto.base.InputConverter;
import com.czxy.redyu.model.entity.Attachment;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Size;

/**
 * Attachment params.
 *
 */
@Data
public class AttachmentParam implements InputConverter<Attachment> {


    @Size(max = 255, message = "附件名称的字符长度不能超过 {max}")
    private String name;

}
