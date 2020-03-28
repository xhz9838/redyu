package com.czxy.redyu;

import com.czxy.redyu.model.dto.UserDTO;
import com.czxy.redyu.utils.JwtUtils;
import com.czxy.redyu.utils.RasUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by liangtong.
 */
@Slf4j
public class TestJwt {
    private static final String pubKeyPath = "D:\\ras\\ras.pub";

    private static final String priKeyPath = "D:\\ras\\ras.pri";


    @Test
    public void testToken() throws Exception {
        UserDTO userInfo = new UserDTO();
        userInfo.setId(10);
        userInfo.setUsername("用户名");
        String token = JwtUtils.generateToken(userInfo, 30, RasUtils.getPrivateKey(priKeyPath));
        System.out.println(token);
    }

    @Test
    public void testParserToken() throws Exception {
        log.error("t={}",1);
        log.debug("w={}",1);
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJhdmF0YXIiOiJodHRwczovL3JlZHl1YmxvZy5vc3MtY24tYmVpamluZy5hbGl5dW5jcy5jb20vcmVkeXUucG5nIiwiY2xhc3MiOiJjb20uY3p4eS5yZWR5dS5tb2RlbC5kdG8uVXNlckRUTyIsImlkIjoxLCJwYXNzd29yZCI6IiQyYSQxMCQvd1Y0aHlqYkhOcU5mdmRvb0FPblhPdWp5cVJFbDlCcGlXUi5VeHpVT21nZGtndGJtMkFrZSIsInVzZXJuYW1lIjoieGh6MTIzIiwiZXhwIjoxNTc2NDU2NzA2fQ.ARgyhQWPKECu2Y1vKqhx2BnonVOHuOkS4sSNiNSw-k-QoI2mRAQp_-McggLt0z6d41bn43QT8zdPx8ukvQcPFBlhwP3Z48rcpxc5CGBcB0d9LjiSObv1OInJSPcREPQZn3qLpJns2gqRk_O5OVUnKXVedlPPpV5DjYcq89bnQAo";
        UserDTO userDTO = JwtUtils.getObjectFromToken(token, RasUtils.getPublicKey(pubKeyPath), UserDTO.class);
        System.out.println(userDTO);
    }
}
