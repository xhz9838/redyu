package com.czxy.redyu.security.handler;

import com.czxy.redyu.exception.RedyuException;
import com.czxy.redyu.model.vo.BaseResponse;
import com.czxy.redyu.utils.ExceptionUtils;
import com.czxy.redyu.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/18
 */
@Component
@Slf4j
public class AdminAuthenticationFailureHandler {

    private boolean productionEnv = false;

    private ObjectMapper objectMapper = JsonUtils.DEFAULT_JSON_MAPPER;
    public void onFailure(HttpServletRequest request, HttpServletResponse response, RedyuException exception) throws IOException {
        log.error("身份验证失败",exception);
        BaseResponse<Object> errorDetail = new BaseResponse<>();

        errorDetail.setStatus(exception.getStatus().value());
        errorDetail.setData(exception.getErrorData());

        if(!productionEnv){
            errorDetail.setDevMessage(ExceptionUtils.getStackTrace(exception));
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(exception.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(errorDetail));
    }

    public boolean isProductionEnv() {
        return productionEnv;
    }

    public void setProductionEnv(boolean productionEnv) {
        this.productionEnv = productionEnv;
    }
}
