package com.custom.view.project.annotation;

/**
 * Created by dsd on 2017/4/11.
 */

public class Demo1 {

    public void m1(){

    }

    @MyTag
    public void m2(){

    }

    @MyTag(name = "张三")
    public void m3(){

    }

    @MyTag(name = "小马", age = 23)
    public void m4(){

    }

}
