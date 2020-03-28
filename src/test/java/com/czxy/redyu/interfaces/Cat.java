package com.czxy.redyu.interfaces;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */
public class Cat implements Animal {
    @Override
    public void eat() {
        System.out.println("猫咪爱吃鱼");
    }

    @Override
    public void run() {
        System.out.println("猫咪会爬树");
    }
}
