package com.czxy.redyu;

import com.czxy.redyu.utils.RasUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by liangtong.
 */
@Slf4j
public class TestRAS {

    private static final String pubKeyPath = "D:\\ras\\ras.pub";

    private static final String priKeyPath = "D:\\ras\\ras.pri";

    @Test
    public void testRas() throws Exception {
        //生产公钥和私钥
        log.error("red={}",1);
        RasUtils.generateKey(pubKeyPath, priKeyPath, "xhz9838");
    }

    @Test
    public void testGetRas() throws Exception {
        //获得公钥和私钥
        PublicKey publicKey = RasUtils.getPublicKey(pubKeyPath);
        PrivateKey privateKey = RasUtils.getPrivateKey(priKeyPath);

        System.out.println(publicKey.toString());
        System.out.println(privateKey.toString());
    }
    @Test
    public void testGenerateToken() throws Exception {
        String str = Jwts.builder()
                .claim("test","测试数据")
                .setExpiration(DateTime.now().plusMinutes(60).toDate())
                .signWith(SignatureAlgorithm.RS256, RasUtils.getPrivateKey(priKeyPath))
                .compact();
        System.out.println(str);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJ0ZXN0Ijoi5rWL6K-V5pWw5o2uIiwiZXhwIjoxNTc2Mjk3NjA4fQ.aRpvXX4T9a0rZcU3HuJd5RaKRJke5A-lQyF6gU5XO7MzLd42TyQ0u_D-7cHLvFBfzpVLrMofX_pjG35C2Fn45kOaNwkup5IQCOtBv1Mjt3MH9vOoqmtTinJ0FPmTdqoppUGWNFnnhbJ5Hw9uWgFX8O4oUm5z67sLI5RYd2cRlUE";
        Claims claims = Jwts.parser().setSigningKey(RasUtils.getPublicKey(pubKeyPath)).
                parseClaimsJws(token).getBody();
        String text = claims.get("test",String.class);
        System.out.println(text);
    }
}