package com.czxy.redyu.service.impl;

import com.czxy.redyu.service.PostService;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Resource
    private PostService postService;
    @Test
    public void blogAdminLineChart() {
        LocalDate nowDay = LocalDate.now().plusDays(-10);
        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            days.add(postService.getVisitsByDays(nowDay.toString()));
            nowDay = nowDay.plusDays(1);
        }
        System.out.println(days);


    }
}