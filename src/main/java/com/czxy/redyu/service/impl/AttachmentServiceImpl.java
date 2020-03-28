package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.AttachmentMapper;
import com.czxy.redyu.handler.file.FileHandlers;
import com.czxy.redyu.model.dto.AttachmentDTO;
import com.czxy.redyu.model.entity.Attachment;
import com.czxy.redyu.model.params.AttachmentQuery;
import com.czxy.redyu.model.support.UploadResult;
import com.czxy.redyu.service.AttachmentService;
import com.czxy.redyu.service.OptionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

    @Resource
    private AttachmentMapper attachmentMapper;

    @Resource
    private OptionService optionService;

    @Resource
    private  FileHandlers fileHandlers;
    @Override
    public PageInfo<AttachmentDTO> listAllByPage(AttachmentQuery attachmentQuery) {
        PageHelper.startPage(attachmentQuery.getPageNum(),attachmentQuery.getPageSize());
        if(attachmentQuery.getName()!=null && !"".equals(attachmentQuery.getName())){
            attachmentQuery.setName("%"+attachmentQuery.getName()+"%");
        }
        List<Attachment> attachmentList = attachmentMapper.listAllByParam(attachmentQuery);

        PageInfo<Attachment> attachmentPageInfo = new PageInfo<>(attachmentList);


        List<AttachmentDTO> attachmentDTOList = attachmentList.stream().map(this::convertToDTO).collect(Collectors.toList());
        PageInfo<AttachmentDTO> attachmentDTOPageInfo = new PageInfo<>(attachmentDTOList);
        BeanUtils.copyProperties(attachmentPageInfo,attachmentDTOPageInfo);

       return attachmentDTOPageInfo;
    }

    @Override
    public AttachmentDTO convertToDTO(Attachment attachment) {

        return new AttachmentDTO().convertFrom(attachment);
    }

    @Override
    public List<String> listAllMediaType() {
        return attachmentMapper.listAllMediaType();
    }

    @Override
    public List<Integer> listTypes() {
        return attachmentMapper.listTypes();
    }


    @Override
    public Attachment upload(MultipartFile file) {

        Assert.notNull(file, "Multipart file 必须不为空");

        Integer attachmentType = optionService.getAttachmentType();
        UploadResult uploadResult = fileHandlers.upload(file, attachmentType);
        log.debug("Upload result: [{}]", uploadResult);

        // Build attachment
        Attachment attachment = new Attachment();
        attachment.setName(uploadResult.getFilename());
        // Convert separator
        attachment.setPath(uploadResult.getFilePath());
        attachment.setFileKey(uploadResult.getKey());
        attachment.setThumbPath(uploadResult.getThumbPath());
        attachment.setMediaType(uploadResult.getMediaType().toString());
        attachment.setSuffix(uploadResult.getSuffix());
        attachment.setWidth(uploadResult.getWidth());
        attachment.setHeight(uploadResult.getHeight());
        attachment.setSize(uploadResult.getSize());
        attachment.setType(attachmentType);

        log.debug("Creating attachment: [{}]", attachment);

        // Create and return
        return create(attachment);
    }

    private Attachment create(Attachment attachment) {
        attachmentMapper.insert(attachment);
        return attachmentMapper.selectByPrimaryKey(attachment.getId());
    }


    @Override
    public AttachmentDTO convertToDto(Attachment attachment) {
        return new AttachmentDTO().convertFrom(attachment);
    }

    @Override
    public List<String> listSuffix() {
        return attachmentMapper.listSuffix();
    }

    @Override
    public Attachment getById(Integer attachmentId) {
        return attachmentMapper.selectByPrimaryKey(attachmentId);
    }

    @Override
    public Attachment update(Attachment attachment) {
         attachmentMapper.updateByPrimaryKeySelective(attachment);
         return attachment;
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        for (Integer id : ids) {
            fileHandlers.delete(attachmentMapper.selectByPrimaryKey(id));
            attachmentMapper.deleteByPrimaryKey(id);
        }

    }
}
