package com.czxy.redyu.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 将名称变为只含英文的工具类
 */
public class SlugUtils {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    /**
     * Slugify string.
     *
     * @param input input string must not be blank
     * @return slug string
     */
    @NonNull
    @Deprecated
    public static String slugify(@NonNull String input) {
        Assert.hasText(input, "Input string must not be blank");

        String withoutWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(withoutWhitespace, Normalizer.Form.NFKD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }


    /**
     * Slugify string.
     *
     * @param input input string must not be blank
     * @return slug string
     */
    public static String slug(@NonNull String input) {
        Assert.hasText(input, "Input string must not be blank");
        String slug = input.
                replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5\\.\\-)]", "").
                replaceAll("[\\?\\\\/:|<>\\*\\[\\]\\(\\)\\$%\\{\\}@~\\.]", "").
                replaceAll("\\s", "")
                .toLowerCase(Locale.ENGLISH);
        return StrUtil.isNotEmpty(slug) ? slug : String.valueOf(System.currentTimeMillis());
    }
}
