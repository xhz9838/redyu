package com.czxy.redyu.service;

import com.czxy.redyu.model.dto.AttachmentDTO;
import com.czxy.redyu.model.entity.Attachment;
import com.czxy.redyu.model.params.AttachmentQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */

public interface AttachmentService {


    PageInfo<AttachmentDTO> listAllByPage(AttachmentQuery attachmentQuery);

    AttachmentDTO convertToDTO(Attachment attachment);

    List<String> listAllMediaType();

    List<Integer> listTypes();

    Attachment upload(MultipartFile file);

    AttachmentDTO convertToDto(Attachment attachment);

    List<String> listSuffix();

    Attachment getById(Integer attachmentId);

    Attachment update(Attachment attachment);

    void deleteByIds(List<Integer> ids);
}
