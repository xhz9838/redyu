package com.czxy.redyu.model.support;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.MediaType;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/19
 */
@Data
@ToString
public class UploadResult {

    private String filename;

    private String filePath;

    private String key;

    private String thumbPath;

    private String suffix;

    private MediaType mediaType;

    private Long size;

    private Integer width;

    private Integer height;

}
