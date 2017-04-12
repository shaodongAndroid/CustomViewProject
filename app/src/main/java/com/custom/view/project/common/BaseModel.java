package com.custom.view.project.common;

import java.io.Serializable;

/**
 * Created by 少东 on 2017/3/7.
 */

public class BaseModel<T> implements Serializable{
    private int code ;

    private String msg ;

    private T data ;

    public boolean isSuccess(){
       return code == 1 ;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
