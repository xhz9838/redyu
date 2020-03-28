package com.czxy.redyu.model.vo;

import com.czxy.redyu.model.enums.ResultEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 *  返回结果实体类
 *   格式：
 *      {
 *          "code": 1,
 *          "message": "登录成功",
 *          "data": { }
 *          }
 *      }
 * @author xuhongzu
 * @version 1.0
 * @date 2019/11/11
 */
@Getter
public class BaseResult {

    //成功状态码
    public static final int OK = 1;
    //失败状态码
    public static final int ERROR = 0;

    //返回码
    private Integer code;
    //返回消息
    private String message;

    //存放数据
    private Object data;
    //其他数据
    private Map<String,Object> other = new HashMap<>();


    public BaseResult( ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }
    public BaseResult(ResultEnum resultEnum, Object data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    /**
     * 快捷成功BaseResult对象
     * @param resultEnum
     * @return
     */
    public static BaseResult ok(ResultEnum resultEnum){
        return new BaseResult(resultEnum);
    }

    public static BaseResult ok(ResultEnum resultEnum, Object data){
        return new BaseResult(resultEnum, data );
    }

    /**
     * 快捷失败BaseResult对象
     * @param resultEnum 信息
     * @return
     */
    public static BaseResult error(ResultEnum resultEnum){
        return new BaseResult(resultEnum);
    }

    /**
     * 自定义数据区域
     * @param key
     * @param msg
     * @return
     */
    public BaseResult append(String key , Object msg){
        other.put(key , msg);
        return this;
    }
}

