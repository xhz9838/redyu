package com.czxy.redyu.model.properties;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public enum QiniuOssProperties implements PropertyEnum {


    OSS_QINIU_REGION("oss_qiniu_region", String.class, "auto"),

    OSS_ACCESS_KEY("oss_qiniu_access_key", String.class, ""),


    OSS_SECRET_KEY("oss_qiniu_secret_key", String.class, ""),


    OSS_SOURCE("oss_qiniu_source", String.class, ""),


    OSS_PROTOCOL("oss_qiniu_domain_protocol", String.class, "https://"),


    OSS_DOMAIN("oss_qiniu_domain", String.class, ""),


    OSS_BUCKET("oss_qiniu_bucket", String.class, ""),


    OSS_STYLE_RULE("oss_qiniu_style_rule", String.class, ""),


    OSS_THUMBNAIL_STYLE_RULE("oss_qiniu_thumbnail_style_rule", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    QiniuOssProperties(String value, Class<?> type, String defaultValue) {
        this.defaultValue = defaultValue;
        if (!PropertyEnum.isSupportedType(type)) {
            throw new IllegalArgumentException("Unsupported blog property type: " + type);
        }

        this.value = value;
        this.type = type;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

}
