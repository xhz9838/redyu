package com.czxy.redyu.interfaces;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */
public class Dog implements Animal {
    @Override
    public void eat() {
        System.out.println("小狗爱吃肉");
    }

    @Override
    public void run() {
        System.out.println("小狗爱蹦蹦跳跳");
    }
}
