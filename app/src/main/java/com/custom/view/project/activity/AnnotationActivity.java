package com.custom.view.project.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.custom.view.project.R;
import com.custom.view.project.annotation.Demo1;
import com.custom.view.project.annotation.MyTag;
import com.custom.view.project.util.Log;
import java.lang.reflect.Method;

import static android.R.attr.name;

public class AnnotationActivity extends AppCompatActivity implements View.OnClickListener{


    public @interface Test {

    }

    public @interface User {
        String name() default "小欧";
        int age() default 20;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_annotation, R.id.tv_annotation1, R.id.tv_annotation2})
    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_annotation:
                Log.d("---------Demo1.class.toString() = "+Demo1.class.toString());
                process("com.custom.view.project.annotation.Demo1");
                break;
            case R.id.tv_annotation1:
                Log.d("---------Demo1.class = "+Demo1.class);
                process_1("com.custom.view.project.annotation.Demo1");
                break;
            case R.id.tv_annotation2:
                process_2("com.custom.view.project.annotation.Demo1");
                // process_2(Demo1.class);
                // process_2(this);
                break;
        }
    }

    public void process(String clazz){
        Class targetClass = null ;

        try {
            targetClass = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for(Method m : targetClass.getMethods()){
            if(m.isAnnotationPresent(MyTag.class)){
                Log.d("-----------annotation = "+"被MyTag注解修饰的方法名：" + m.getName());
            } else {
                Log.d("-----------annotation = "+"--NO--被MyTag注解修饰的方法名：" + m.getName());
            }
        }
    }
    public void process_1(String clazz){
        Class targetClass = null ;
        try {
            targetClass = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for(Method m : targetClass.getMethods()){
            if(m.isAnnotationPresent(MyTag.class)){
                MyTag myTag = m.getAnnotation(MyTag.class);
                Log.d("------tag = "+"方法" + m.getName() + "--的MyTag注解内容为：" + myTag.name() + "，" + myTag.age());
            }
        }
    }
    public void process_2(String clazz){
        Class targetClass = null;

        try {
            targetClass = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("--------isAnnotation = "+targetClass.isAnnotation());

        String clsName = targetClass.getName();
        Log.d("--------clsName = "+clsName);

        if(!clsName.startsWith("android.") && !clsName.startsWith("java.")) {
            try {
                Class e = Class.forName(clsName + "_ViewBinding");
                // bindingCtor = e.getConstructor(new Class[]{cls, View.class});
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }


        for(Method m : targetClass.getMethods()){
            if(m.isAnnotationPresent(MyTag.class)){
                MyTag myTag = m.getAnnotation(MyTag.class);
                Log.d("------tag = "+"方法" + m.getName() + "--的MyTag注解内容为：" + myTag.name() + "，" + myTag.age());
            }
        }
    }

    @Test
    public void testInfo(){

    }

    @User(name="李治", age = 25)
    public void getUser(){

    }

    @User
    public void getUser1(){

    }

}

