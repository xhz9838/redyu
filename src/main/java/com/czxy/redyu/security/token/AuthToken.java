package com.czxy.redyu.security.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/14
 */
@Data
public class AuthToken {

    @JsonProperty("access_token")
    private String accessToken;


    @JsonProperty("expired_in")
    private int expiredIn;


}
