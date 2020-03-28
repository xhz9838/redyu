package com.czxy.redyu;

import com.czxy.redyu.config.BCrypt;
import com.czxy.redyu.utils.RedisUtil;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TestDemo {
    @Resource
    RedisUtil redisUtil;

    /**
     * DateTimeFormatter和LocalDateTime
     * DateTimeFormatter线程安全，建议使用
     * LocalDateTime  jdk 1.8新特性，功能更强大
     */
    @Test
    public void demoTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("1998-04-03 09:30:25", formatter);
        String format = localDateTime.format(formatter);
        System.out.println(format);
    }

    @Test
    public void demo() {
        String hashpw = BCrypt.hashpw("a199838");
        System.out.println(hashpw);
        boolean b = BCrypt.checkpw("a199838", hashpw);
        System.out.println(b);
    }

    @Test
    public void demoTest1() {
        boolean blank = StringUtils.isNotBlank(" 2");
        System.out.println(blank);
    }

    @Test
    public void demoTest2() {
        Date date1 = new Date();
        date1.setYear(2017);
        Date date2 = new Date();
        System.out.println(date1.after(date2));
    }

    @Test
    public void demoTest3() throws IOException {

        File file = new File("C:\\Users\\Administrator\\Pictures\\1.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        int i2 = 0;
        String st = "";
        while ((i2 = inputStream.read(bytes)) != -1) {

            st = new String(bytes, 0, i2);// AB CD
        }
        inputStream.close();
        FileInputStream is2 = new FileInputStream(st);

    }

    @Test
    public void demoTestInterface(){

        redisUtil.set("11",22);
        Object o = redisUtil.get("11");
        System.out.println(o);
    }

    @Test
    public void demo10(){
        String str = "na0";
        Configuration cfg = null;
        switch (str){
            case "z0":
                cfg = new Configuration(Region.region0());
                System.out.println("z0");
                break;
            case "z1":
                cfg = new Configuration(Region.region1());
                System.out.println("z1");
                break;
            case "z2":
                cfg = new Configuration(Region.region2());
                System.out.println("z2");
                break;
            case "na0":
                cfg=new Configuration(Region.regionNa0());
                System.out.println("na0");
                break;
            case "as0":
                cfg = new Configuration(Region.regionAs0());
                System.out.println("as0");
                break;
             default:
                 cfg = new Configuration(Region.autoRegion());
                 System.out.println("auto");
        }

    }
    @Test
    public void demo11(){
        int[] arr = {11,44,22,33,77,45,32,88};
        long t1 = System.currentTimeMillis();
        for (int i =0;i<arr.length-1;i++) {
            for (int j =i+1;j<arr.length ; j++) {
              if(arr[j]<arr[i]){
                int t = arr[i];
                arr[i]=arr[j];
                arr[j]=t;
              }
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void demo12(){
        String baseUrl = "https://cdn.v2ex.com/gravatar/";
         String utl = baseUrl+ DigestUtils.md5DigestAsHex("15387066867@163.com".getBytes())+"?s=65&r=G&d=";

        System.out.println(utl);
    }
    @Test
    public void demo13(){
        String property = System.getProperties().getProperty("java.io.tmpdir");
        System.out.println(property);
    }
    @Test
    public void demo14(){
        LocalDate nowDay = LocalDate.now();
        List<LocalDate> days = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            days.add(nowDay);
            nowDay = nowDay.plusDays(-1);
        }

        System.out.println(days);
    }
}
