package com.czxy.redyu.utils;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public class ServletUtils {

    private ServletUtils() {
    }

    /**
     * Gets current http servlet request.
     *
     * @return an optional http servlet request
     */
    @NonNull
    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(requestAttributes -> requestAttributes instanceof ServletRequestAttributes)
                .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
                .map(ServletRequestAttributes::getRequest);
    }

    /**
     * Gets request ip.
     *
     * @return ip address or null
     */
    @Nullable
    public static String getRequestIp() {
        return getCurrentRequest().map(ServletUtil::getClientIP).orElse(null);
    }

    /**
     * Gets request header.
     *
     * @param header http header name
     * @return http header of null
     */
    @Nullable
    public static String getHeaderIgnoreCase(String header) {
        return getCurrentRequest().map(request -> ServletUtil.getHeaderIgnoreCase(request, header)).orElse(null);
    }

}
