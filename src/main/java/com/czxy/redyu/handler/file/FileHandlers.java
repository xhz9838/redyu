package com.czxy.redyu.handler.file;

import com.czxy.redyu.exception.FileOperationException;
import com.czxy.redyu.model.entity.Attachment;
import com.czxy.redyu.model.support.UploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/22
 */
@Slf4j
@Component
public class FileHandlers {


    private final Collection<FileHandler> fileHandlers = new LinkedList<>();

    public FileHandlers(ApplicationContext applicationContext) {

        addFileHandlers(applicationContext.getBeansOfType(FileHandler.class).values());
    }


    @NonNull
    public UploadResult upload(@NonNull MultipartFile file, @NonNull Integer attachmentType) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(attachmentType, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(attachmentType)) {
                return fileHandler.upload(file);
            }
        }

        throw new FileOperationException("No available file handler to upload the file").setErrorData(attachmentType);
    }


    public void delete( Attachment attachment) {
        Assert.notNull(attachment, "Attachment must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(attachment.getType())) {
                // Delete the file
                fileHandler.delete(attachment.getFileKey());
                return;
            }
        }

        throw new FileOperationException("No available file handler to delete the file").setErrorData(attachment);
    }



    public FileHandlers addFileHandlers( Collection<FileHandler> fileHandlers) {
        if (!CollectionUtils.isEmpty(fileHandlers)) {
            this.fileHandlers.addAll(fileHandlers);
        }
        return this;
    }
}