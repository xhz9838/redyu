package com.czxy.redyu.model.properties;

import com.czxy.redyu.model.enums.ValueEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface PropertyEnum extends ValueEnum<String> {


    @SuppressWarnings("unchecked")
    static <T> T convertTo(@NonNull String value, @NonNull Class<T> type) {
        Assert.notNull(value, "Value must not be null");
        Assert.notNull(type, "Type must not be null");

        if (type.isAssignableFrom(String.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(value);
        }

        if (type.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(value);
        }

        if (type.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(value);
        }

        if (type.isAssignableFrom(Short.class)) {
            return (T) Short.valueOf(value);
        }

        if (type.isAssignableFrom(Byte.class)) {
            return (T) Byte.valueOf(value);
        }

        if (type.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(value);
        }

        if (type.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(value);
        }

        throw new UnsupportedOperationException("Unsupported convention for blog property type:" + type.getName() + " provided");
    }


    @SuppressWarnings("unchecked")
    static Object convertTo(@Nullable String value, @NonNull PropertyEnum propertyEnum) {
        Assert.notNull(propertyEnum, "Property enum must not be null");

        if (StringUtils.isBlank(value)) {

            value = propertyEnum.defaultValue();
        }

        try {
            if (propertyEnum.getType().isAssignableFrom(Enum.class)) {
                Class<Enum> type = (Class<Enum>) propertyEnum.getType();
                Enum result = convertToEnum(value, type);
                return result != null ? result : value;
            }

            return convertTo(value, propertyEnum.getType());
        } catch (Exception e) {

            return value;
        }
    }



    static <T extends Enum<T>> T convertToEnum(@NonNull String value, @NonNull Class<T> type) {
        Assert.hasText(value, "Property value must not be blank");

        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (Exception e) {

            return null;
        }
    }


    static boolean isSupportedType(Class<?> type) {
        return type != null && (
                type.isAssignableFrom(String.class)
                        || type.isAssignableFrom(Number.class)
                        || type.isAssignableFrom(Integer.class)
                        || type.isAssignableFrom(Long.class)
                        || type.isAssignableFrom(Boolean.class)
                        || type.isAssignableFrom(Short.class)
                        || type.isAssignableFrom(Byte.class)
                        || type.isAssignableFrom(Double.class)
                        || type.isAssignableFrom(Float.class)
                        || type.isAssignableFrom(Enum.class)
                        || type.isAssignableFrom(ValueEnum.class)
        );
    }

    static Map<String, PropertyEnum> getValuePropertyEnumMap() {
        // Get all properties
        List<Class<? extends PropertyEnum>> propertyEnumClasses = new LinkedList<>();

        propertyEnumClasses.add(QiniuOssProperties.class);

        Map<String, PropertyEnum> result = new HashMap<>();

        propertyEnumClasses.forEach(propertyEnumClass -> {
            PropertyEnum[] propertyEnums = propertyEnumClass.getEnumConstants();

            for (PropertyEnum propertyEnum : propertyEnums) {
                result.put(propertyEnum.getValue(), propertyEnum);
            }
        });

        return result;
    }


    Class<?> getType();


    String defaultValue();
}
