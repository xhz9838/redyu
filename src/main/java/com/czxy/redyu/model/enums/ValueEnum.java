package com.czxy.redyu.model.enums;

import org.springframework.util.Assert;

import java.util.stream.Stream;

/**
 * 枚举值的接口
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */
public interface ValueEnum<T>{

    static <V, E extends ValueEnum<V>> E valueToEnum(Class<E> enumType, V value){
        Assert.notNull(enumType,"枚举类型不能为null");
        Assert.notNull(value,"值必须不为null");
        Assert.isTrue(enumType.isEnum(),"类型必须是枚举类型");

        return Stream.of(enumType.getEnumConstants())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的数据:"+value));
    }
    T getValue();
}
