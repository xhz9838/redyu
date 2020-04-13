package com.czxy.redyu.controller.admin;

import com.czxy.redyu.model.dto.AttachmentDTO;
import com.czxy.redyu.model.entity.Attachment;
import com.czxy.redyu.model.params.AttachmentParam;
import com.czxy.redyu.model.params.AttachmentQuery;
import com.czxy.redyu.service.AttachmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @ date2019/12/10
 */
@RestController
@RequestMapping("/admin/attachment")
public class AttachmentController {

    @Resource
    private AttachmentService attachmentService;


    @PostMapping
    public PageInfo<AttachmentDTO> listAllByPage(@RequestBody AttachmentQuery attachmentQuery){
       return attachmentService.listAllByPage(attachmentQuery);
    }

    @GetMapping("/media_types")
    public List<String> listMediaTypes(){
        return attachmentService.listAllMediaType();
    }
    @GetMapping("/suffix")
    public List<String> listSuffix(){
        return attachmentService.listSuffix();
    }

    @GetMapping("/list_types")
    public List<Integer> listTypes(){
        return attachmentService.listTypes();
    }
    @PostMapping(value = "uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<AttachmentDTO> uploadAttachments(@RequestPart("files") MultipartFile[] files) {
        List<AttachmentDTO> result = new LinkedList<>();

        for (MultipartFile file : files) {

            Attachment attachment = attachmentService.upload(file);

            result.add(attachmentService.convertToDto(attachment));
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        return result;
    }

    @PutMapping("/{attachmentId}")
    public AttachmentDTO updateAttachment(@PathVariable Integer attachmentId,
                                          @RequestBody AttachmentParam attachmentParam){
        Attachment attachment = attachmentService.getById(attachmentId);
        attachmentParam.update(attachment);
        return new AttachmentDTO().convertFrom(attachmentService.update(attachment));
    }

    @DeleteMapping("/{ids}")
    public void deleteByIds(@PathVariable List<Integer> ids){
        attachmentService.deleteByIds(ids);
    }
}
