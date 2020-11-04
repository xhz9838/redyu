package com.czxy.redyu.handler.file;

import com.czxy.redyu.model.support.UploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import static com.czxy.redyu.model.support.RedyuConst.FILE_SEPARATOR;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface FileHandler {

    MediaType IMAGE_TYPE = MediaType.valueOf("image/*");

     static boolean isImageType( MediaType mediaType) {
        return mediaType != null && IMAGE_TYPE.includes(mediaType);
    }



    static String normalizeDirectory( String dir) {
        Assert.hasText(dir, "Directory full name must not be blank");

        return StringUtils.appendIfMissing(dir, FILE_SEPARATOR);
    }



    UploadResult upload( MultipartFile file);


    void delete( String key);


    boolean supportType(Integer type);
}
