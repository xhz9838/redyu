package com.czxy.redyu.utils;

import com.czxy.redyu.exception.BeanUtilsException;
import lombok.NonNull;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public class BeanUtils {

    private BeanUtils() {
    }


    public static <T> T transformFrom( Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T targetInstance = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, targetInstance, getNullPropertyNames(source));
            return targetInstance;
        } catch (Exception e) {
            throw new BeanUtilsException("Failed to new " + targetClass.getName() + " instance or copy properties", e);
        }
    }


    public static <T> List<T> transformFromInBatch(Collection<?> sources, @NonNull Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        return sources.stream()
                .map(source -> transformFrom(source, targetClass))
                .collect(Collectors.toList());
    }
    public static void updateProperties(@NonNull Object source, @NonNull Object target) {

        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch (BeansException e) {
            throw new BeanUtilsException("Failed to copy properties", e);
        }
    }
    private static String[] getNullPropertyNames(@NonNull Object source) {
        return getNullPropertyNameSet(source).toArray(new String[0]);
    }


    private static Set<String> getNullPropertyNameSet(@NonNull Object source) {

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
            if (propertyValue == null) {
                emptyNames.add(propertyName);
            }
        }
        return emptyNames;
    }
}
