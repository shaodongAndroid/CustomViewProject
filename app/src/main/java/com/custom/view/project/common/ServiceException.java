package com.custom.view.project.common;

/**
 * Created by 少东 on 2017/3/7.
 */

public class ServiceException extends Exception {

    private String msg ;

    public ServiceException(String msg){
        this.msg = msg ;
    }

}
