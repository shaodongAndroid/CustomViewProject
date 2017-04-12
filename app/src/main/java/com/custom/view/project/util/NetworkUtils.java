package com.custom.view.project.util;

import com.custom.view.project.common.BaseModel;
import com.custom.view.project.common.ServiceException;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 少东 on 2017/3/7.
 */

public class NetworkUtils {
    public static <T> FlowableTransformer<BaseModel<T>, T> handResult(){
        return new FlowableTransformer<BaseModel<T>, T>() {
            @Override
            public Publisher<T> apply(final Flowable<BaseModel<T>> flowable) {
                return flowable.flatMap(new Function<BaseModel<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(BaseModel<T> result) throws Exception {
                        if(result.isSuccess()){
                            return createData(result.getData());
                        } else {
                            //TODO
                            return (Publisher<T>) Observable.error(new ServiceException(result.getMsg()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T>Publisher<T> createData(final T data) {
        return new Publisher<T>() {
            @Override
            public void subscribe(Subscriber<? super T> subscriber) {
                try{
                    subscriber.onNext(data);
                    subscriber.onComplete();
                } catch (Exception e){
                    subscriber.onError(e);
                }
            }
        } ;
    }


    public static <T>ObservableTransformer<BaseModel<T>, T> handleResult(){
       return new ObservableTransformer<BaseModel<T>, T>() {
           @Override
           public ObservableSource<T> apply(Observable<BaseModel<T>> observable) {
               return observable.flatMap(new Function<BaseModel<T>, ObservableSource<T>>() {
                   @Override
                   public ObservableSource<T> apply(BaseModel<T> result) throws Exception {
                       if(result.isSuccess()){
                           return createDataByObservable(result.getData());
                       }
                       return Observable.error(new ServiceException(result.getMsg()));
                   }
               }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
           }

       };
    }
    private static <T>ObservableSource<T> createDataByObservable(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> observable) throws Exception {
                try {
                    observable.onNext(data);
                    observable.onComplete();
                } catch (Exception e){
                    observable.onError(e);
                }
            }
        });
    }


}
