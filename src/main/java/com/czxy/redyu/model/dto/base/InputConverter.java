package com.czxy.redyu.model.dto.base;


import com.czxy.redyu.utils.BeanUtils;
import com.czxy.redyu.utils.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface InputConverter<DOMAIN> {


    @SuppressWarnings("unchecked")
    default DOMAIN convertTo() {

        ParameterizedType currentType = parameterizedType();


        Objects.requireNonNull(currentType, "Cannot fetch actual type because parameterized type is null");

        Class<DOMAIN> domainClass = (Class<DOMAIN>) currentType.getActualTypeArguments()[0];

        return BeanUtils.transformFrom(this, domainClass);
    }


    default void update(DOMAIN domain) {
        BeanUtils.updateProperties(this, domain);
    }



    default ParameterizedType parameterizedType() {
        return ReflectionUtils.getParameterizedType(InputConverter.class, this.getClass());
    }
}

