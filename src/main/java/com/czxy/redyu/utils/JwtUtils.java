package com.czxy.redyu.utils;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/14
 */
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by liangtong.
 */
public class JwtUtils {
    /**
     *  私钥加密token
     * @param data 需要加密的数据（载荷内容）
     * @param expireMinutes 过期时间，单位：分钟
     * @param privateKey 私钥
     * @return
     */
    public static String generateToken(Object data, int expireMinutes, PrivateKey privateKey) throws Exception {
        //1 获得jwt构建对象
        JwtBuilder jwtBuilder = Jwts.builder();
        //2 设置数据
        if( data == null ) {
            throw new RuntimeException("数据不能为空");
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            // 获得属性名
            String name = propertyDescriptor.getName();
            // 获得属性值
            Object value = propertyDescriptor.getReadMethod().invoke(data);
            if(value != null) {
                jwtBuilder.claim(name,value);
            }
        }
        //3 设置过期时间
        jwtBuilder.setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate());
        //4 设置加密
        jwtBuilder.signWith(SignatureAlgorithm.RS256, privateKey);
        //5 构建
        return jwtBuilder.compact();
    }

    /**
     * 通过公钥解析token
     * @param token 需要解析的数据
     * @param publicKey 公钥
     * @param beanClass 封装的JavaBean
     * @return
     * @throws Exception
     */
    public static <T> T  getObjectFromToken(String token, PublicKey publicKey,Class<T> beanClass) throws Exception {
        //1 获得解析后内容
        Claims body = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        //2 将内容封装到对象JavaBean
        T bean = beanClass.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            // 获得属性名
            String name = propertyDescriptor.getName();
            // 通过属性名，获得对应解析的数据
            Object value = body.get(name);
            if(value != null) {
                // 将获得的数据封装到对应的JavaBean中
                BeanUtils.setProperty(bean,name,value);
            }
        }
        return bean;
    }
}