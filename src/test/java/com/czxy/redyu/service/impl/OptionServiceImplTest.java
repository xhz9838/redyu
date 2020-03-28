package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.OptionMapper;
import com.czxy.redyu.model.entity.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OptionServiceImplTest {

    @Resource
    private OptionMapper optionMapper;
    @Test
    public void operatingTime() {
        Example example = new Example(Option.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("optionKey","birthday");
        Option option = optionMapper.selectOneByExample(example);
        long newTime = System.currentTimeMillis();
        Long OperateTime = newTime-Long.parseLong(option.getOptionValue());
        Long day = OperateTime/(1000*60*60*24);
        System.out.println(day);
    }
}