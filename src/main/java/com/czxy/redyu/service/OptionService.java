package com.czxy.redyu.service;

import com.czxy.redyu.model.properties.PropertyEnum;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */

public interface OptionService {
    String OPTIONS_KEY = "options";

    Integer getAttachmentType();

    @NonNull
    Object getByPropertyOfNonNull(@NonNull PropertyEnum property);

    @NonNull
    Object getByKeyOfNonNull(@NonNull String key);

    @NonNull
    Optional<Object> getByKey(@NonNull String key);

    @NonNull
    Map<String, Object> listOptions();

    <T> T getByPropertyOrDefault(@NonNull PropertyEnum property, @NonNull Class<T> propertyType, T defaultValue);

    /**
     * Gets property value by blog property.
     *
     * @param property     blog property must not be null
     * @param propertyType property type must not be null
     * @param <T>          property type
     * @return property value
     */
    <T> Optional<T> getByProperty(@NonNull PropertyEnum property, @NonNull Class<T> propertyType);

    /**
     * Gets option value by blog property.
     *
     * @param property blog property must not be null
     * @return an optional option value
     */
    @NonNull
    Optional<Object> getByProperty(@NonNull PropertyEnum property);



    Integer OperatingTime();
}
