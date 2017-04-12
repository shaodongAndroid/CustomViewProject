package com.custom.view.project.single;

/**
 * Created by 少东 on 2016/12/9.
 */

/**
 * volatile 这保证了：当某线程修改value的值时，其他线程看到的value值都是最新的，即修改之后的volatile的值
 */

public class SingletonB {
    /**
     * volatile 关键字的必要性,如果没有 volatile 关键字,问题可能会出在singleton = new Singleton() 这句，用伪代码表示：
     * 分配内存 inst = new allocat();
     * 赋值 mInstance = inst ;
     * 真正执行构造函数 constructor(inst) ;
     * 可能会由于虚拟机的优化等导致赋值操作先执行，而构造函数还没完成，
     * 导致其他线程访问得到 singleton 变量不为null，但初始化还未完成，导致程序崩溃
     */

    private volatile static SingletonB mInstance ;

    private SingletonB(){}

    public static SingletonB getmInstance(){
        if(mInstance == null){
            synchronized (SingletonB.class){
                if(mInstance == null){
                    mInstance = new SingletonB();
                }
            }
        }

        return mInstance ;
    }
}
