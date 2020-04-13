package com.czxy.redyu.service;

import com.czxy.redyu.model.properties.PropertyEnum;

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


    Object getByPropertyOfNonNull(PropertyEnum property);


    Object getByKeyOfNonNull( String key);


    Optional<Object> getByKey( String key);


    Map<String, Object> listOptions();

    <T> T getByPropertyOrDefault( PropertyEnum property,  Class<T> propertyType, T defaultValue);


    <T> Optional<T> getByProperty(PropertyEnum property,  Class<T> propertyType);



    Optional<Object> getByProperty( PropertyEnum property);



    Integer OperatingTime();
}
