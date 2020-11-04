package com.czxy.redyu.handler.file;

import com.czxy.redyu.exception.FileOperationException;
import com.czxy.redyu.model.properties.QiniuOssProperties;
import com.czxy.redyu.model.support.QiNiuPutSet;
import com.czxy.redyu.model.support.UploadResult;
import com.czxy.redyu.service.OptionService;
import com.czxy.redyu.utils.FilenameUtils;
import com.czxy.redyu.utils.JsonUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/19
 */

@Slf4j
@Component
public class QiniuOssFileHandler implements FileHandler {

    private final OptionService optionService;

    public QiniuOssFileHandler(OptionService optionService) {
        this.optionService = optionService;
    }

    @Override
    public UploadResult upload(MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");


        String accessKey = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_ACCESS_KEY).toString();
        String secretKey = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_SECRET_KEY).toString();
        String region = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_QINIU_REGION).toString();

        String bucket = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_BUCKET).toString();
        String protocol = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_PROTOCOL).toString();
        String domain = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_DOMAIN).toString();
        String source = optionService.getByPropertyOrDefault(QiniuOssProperties.OSS_SOURCE, String.class, "");
        String styleRule = optionService.getByPropertyOrDefault(QiniuOssProperties.OSS_STYLE_RULE, String.class, "");
        String thumbnailStyleRule = optionService.getByPropertyOrDefault(QiniuOssProperties.OSS_THUMBNAIL_STYLE_RULE, String.class, "");


        Configuration cfg = null;
        switch (region) {
            case "z0":
                cfg = new Configuration(Region.region0());
                break;
            case "z1":
                cfg = new Configuration(Region.region1());
                break;
            case "z2":
                cfg = new Configuration(Region.region2());
                break;
            case "na0":
                cfg = new Configuration(Region.regionNa0());
                break;
            case "as0":
                cfg = new Configuration(Region.regionAs0());
                break;
            default:
                cfg = new Configuration(Region.autoRegion());
        }


        Auth auth = Auth.create(accessKey, secretKey);

        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"size\":$(fsize), " +
                "\"width\":$(imageInfo.width), " +
                "\"height\":$(imageInfo.height)," +
                " \"key\":\"$(key)\", " +
                "\"hash\":\"$(etag)\"}");

        String uploadToken = auth.uploadToken(bucket, null, 3600, putPolicy);


        Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), bucket);

        StringBuilder basePath = new StringBuilder(protocol)
                .append(domain)
                .append("/");

        try {
            String basename = FilenameUtils.getBasename(file.getOriginalFilename());
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String timestamp = String.valueOf(System.currentTimeMillis());
            StringBuilder upFilePath = new StringBuilder();
            if (StringUtils.isNotEmpty(source)) {
                upFilePath.append(source)
                        .append("/");
            }
            upFilePath.append(basename)
                    .append("_")
                    .append(timestamp)
                    .append(".")
                    .append(extension);


            FileRecorder fileRecorder = new FileRecorder(tmpPath.toFile());

            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);

            Response response = uploadManager.put(file.getInputStream(), upFilePath.toString(), uploadToken, null, null);

            log.debug("QnYun response: [{}]", response.toString());
            log.debug("QnYun response body: [{}]", response.bodyString());

            response.jsonToObject(QiNiuPutSet.class);


            QiNiuPutSet putSet = JsonUtils.jsonToObject(response.bodyString(), QiNiuPutSet.class);


            String filePath = StringUtils.join(basePath.toString(), upFilePath.toString());


            UploadResult result = new UploadResult();
            result.setFilename(basename);
            result.setFilePath(StringUtils.isBlank(styleRule) ? filePath : filePath + styleRule);
            result.setKey(upFilePath.toString());
            result.setSuffix(extension);
            result.setWidth(putSet.getWidth());
            result.setHeight(putSet.getHeight());
            result.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            result.setSize(file.getSize());
            result.setThumbPath(filePath);
            return result;
        } catch (IOException e) {
            if (e instanceof QiniuException) {
                log.error("QnYun error response: [{}]", ((QiniuException) e).response);
            }

            throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到七牛云失败", e);
        }
    }

    @Override
    public void delete(String key) {
        Assert.notNull(key, "File key must not be blank");

        String accessKey = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_ACCESS_KEY).toString();
        String secretKey = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_SECRET_KEY).toString();
        String bucket = optionService.getByPropertyOfNonNull(QiniuOssProperties.OSS_BUCKET).toString();


        Configuration configuration = new Configuration(Region.autoRegion());


        Auth auth = Auth.create(accessKey, secretKey);

        BucketManager bucketManager = new BucketManager(auth, configuration);

        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException e) {
            log.error("QnYun error response: [{}]", e.response);
            throw new FileOperationException("附件 " + key + " 从七牛云删除失败", e);
        }
    }

    @Override
    public boolean supportType(Integer type) {
        return 1 == type;
    }
}

