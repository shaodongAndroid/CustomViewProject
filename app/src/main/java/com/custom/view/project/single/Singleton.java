package com.custom.view.project.single;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by 少东 on 2016/12/9.
 */

/**
 * current 是 volatile 类型。这保证了：当某线程修改value的值时，其他线程看到的value值都是最新的，即修改之后的volatile的值。
 * 通过 CAS 设置 value。这保证了：当某线程池通过CAS函数(如compareAndSet函数)设置 value 时，它的操作是原子的，
 * 即线程在操作value时不会被中断。CAS是一种无阻塞的锁,采用不断比较设值的方式来避免并发问题,不会有锁的等待和上下文切换问题,性能消耗较小。
 */
public class Singleton {
    private static final AtomicReference<Singleton> INSTANCE = new AtomicReference<>();

    private Singleton(){}

    public Singleton getInstance(){
        for(;;){
            Singleton current = INSTANCE.get();
            if(current != null){
                return current ;
            }
            current = new Singleton();
            if(INSTANCE.compareAndSet(null, current)){
                return current ;
            }
        }
    }
}
