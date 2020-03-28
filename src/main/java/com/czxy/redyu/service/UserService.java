package com.czxy.redyu.service;

import com.czxy.redyu.model.dto.BlogAdminLineChart;
import com.czxy.redyu.model.dto.BlogInformation;
import com.czxy.redyu.model.params.LoginParam;
import com.czxy.redyu.security.token.AuthToken;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface UserService {



    AuthToken authenticate(LoginParam loginParam);

    BlogInformation blogInformation();

    BlogAdminLineChart blogAdminLineChart();
}
