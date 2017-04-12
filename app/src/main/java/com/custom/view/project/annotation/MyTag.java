package com.custom.view.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dsd on 2017/4/11.
 */

/**
 * Retention用于指定Annotation可以保留多长时间
 * @Retention包含一个名为“value”的成员变量，该value成员变量是RetentionPolicy枚举类型。使用@Retention时，
 * 必须为其value指定值。value成员变量的值只能是如下3个：
 *
 * RetentionPolicy.SOURCE：Annotation只保留在源代码中，编译器编译时，直接丢弃这种Annotation。
 *
 * RetentionPolicy.CLASS：编译器把Annotation记录在class文件中。当运行Java程序时，JVM中不再保留该Annotation。
 *
 * RetentionPolicy.RUNTIME：编译器把Annotation记录在class文件中。当运行Java程序时，JVM会保留该Annotation，
 * 程序可以通过反射获取该Annotation的信息。
 */

//name=value形式
// @Retention(value=RetentionPolicy.RUNTIME)

//直接指定
// @Retention(RetentionPolicy.RUNTIME)
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface MyTag {
    String name() default "dsd" ;

    int age() default 26 ;
}
