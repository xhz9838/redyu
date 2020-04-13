package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.OptionMapper;
import com.czxy.redyu.exception.MissingPropertyException;
import com.czxy.redyu.model.entity.Option;
import com.czxy.redyu.model.properties.PropertyEnum;
import com.czxy.redyu.service.OptionService;
import com.czxy.redyu.utils.RedisUtil;
import com.czxy.redyu.utils.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
public class OptionServiceImpl implements OptionService {
    @Resource
    private OptionMapper optionMapper;

    private final Map<String, PropertyEnum> propertyEnumMap = Collections.unmodifiableMap(PropertyEnum.getValuePropertyEnumMap());

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Integer getAttachmentType() {
        Example example = new Example(Option.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("optionKey", "attachment_type");
        Option option = optionMapper.selectOneByExample(example);
        return Integer.parseInt(option.getOptionValue());

    }

    @Override
    public Object getByPropertyOfNonNull(PropertyEnum property) {
        Assert.notNull(property, "Blog property must not be null");

        return getByKeyOfNonNull(property.getValue());
    }

    @Override
    public Object getByKeyOfNonNull(String key) {
        return getByKey(key).orElseThrow(() -> new MissingPropertyException("You have to config " + key + " setting"));
    }

    @Override
    public Optional<Object> getByKey(String key) {
        Assert.hasText(key, "Option key must not be blank");

        return Optional.ofNullable(listOptions().get(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> listOptions() {

        return Optional.ofNullable((Map) redisUtil.get(OPTIONS_KEY)).orElseGet(() -> {
            List<Option> options = optionMapper.selectAll();

            Set<String> keys = ServiceUtils.fetchProperty(options, Option::getOptionKey);

            Map<String, Object> userDefinedOptionMap = ServiceUtils.convertToMap(options, Option::getOptionKey, option -> {
                String key = option.getOptionKey();

                PropertyEnum propertyEnum = propertyEnumMap.get(key);

                if (propertyEnum == null) {
                    return option.getOptionValue();
                }

                return PropertyEnum.convertTo(option.getOptionValue(), propertyEnum);
            });

            Map<String, Object> result = new HashMap<>(userDefinedOptionMap);

            propertyEnumMap.keySet()
                    .stream()
                    .filter(key -> !keys.contains(key))
                    .forEach(key -> {
                        PropertyEnum propertyEnum = propertyEnumMap.get(key);

                        if (StringUtils.isBlank(propertyEnum.defaultValue())) {
                            return;
                        }
                        result.put(key, PropertyEnum.convertTo(propertyEnum.defaultValue(), propertyEnum));
                    });

            // 将结果集存入缓存
            redisUtil.set(OPTIONS_KEY, result);

            return result;
        });
    }

    @Override
    public <T> T getByPropertyOrDefault(PropertyEnum property, Class<T> propertyType, T defaultValue) {
        Assert.notNull(property, "Blog property must not be null");

        return getByProperty(property, propertyType).orElse(defaultValue);
    }

    @Override
    public <T> Optional<T> getByProperty(PropertyEnum property, Class<T> propertyType) {
        return getByProperty(property).map(propertyValue -> PropertyEnum.convertTo(propertyValue.toString(), propertyType));
    }


    @Override
    public Optional<Object> getByProperty(PropertyEnum property) {
        Assert.notNull(property, "Blog property must not be null");

        return getByKey(property.getValue());
    }

    @Override
    public Integer OperatingTime() {
        Example example = new Example(Option.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("optionKey", "birthday");
        Option option = optionMapper.selectOneByExample(example);
        long newTime = System.currentTimeMillis();
        Long OperateTime = newTime - Long.parseLong(option.getOptionValue());
        Long day = OperateTime / (1000 * 60 * 60 * 24);
        return day.intValue();
    }
}
