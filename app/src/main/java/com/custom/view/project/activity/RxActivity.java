package com.custom.view.project.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.custom.view.project.R;
import com.custom.view.project.common.Course;
import com.custom.view.project.common.UserInfo;
import com.custom.view.project.util.Log;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class RxActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.iv_image)
    ImageView imageView;

    CompositeDisposable mComDisposable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        ButterKnife.bind(this);
        initView();
        mComDisposable = new CompositeDisposable();
    }

    private void initView() {
        findViewById(R.id.btn_observable).setOnClickListener(this);
        findViewById(R.id.btn_scheduler).setOnClickListener(this);
        findViewById(R.id.btn_just_or_from).setOnClickListener(this);
        findViewById(R.id.btn_map).setOnClickListener(this);
        findViewById(R.id.btn_flat_map).setOnClickListener(this);
        findViewById(R.id.btn_zip).setOnClickListener(this);
        findViewById(R.id.btn_zip_1).setOnClickListener(this);
        findViewById(R.id.btn_flowable).setOnClickListener(this);
        findViewById(R.id.btn_time).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_observable:
                useObservable();
                break;
            case R.id.btn_just_or_from:
                useJustOrFrom();
                break;
            case R.id.btn_scheduler:
                useScheduler();
                break;
            case R.id.btn_map:
                useMap();
                break;
            case R.id.btn_flat_map:
                useFlatMap();
                break;
            case R.id.btn_zip:
                useZip();
                break;
            case R.id.btn_zip_1:
                useZip_1();
                break;
            case R.id.btn_flowable:
                useFlowable();
                break;
            case R.id.btn_time:
                useTime();
                break;
        }
    }


    private void useTime() {
    }

    /**
     * observable --> observer
     * flowable --> subscriber
     */
    private void useFlowable() {
        /**
         * Drop就是直接把存不下的事件丢弃,Latest就是只保留最新的事件
         * BackpressureStrategy.ERROR <= 128
         * BackpressureStrategy.BUFFER <
         * BackpressureStrategy.DROP 直接把存不下的事件丢弃
         * BackpressureStrategy.LATEST 只保留最新的事件
         */
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for(int i=0; i < 1000; i++){
                    Log.d("------------emit i = "+i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d("------------onSubscribe()");
//                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d("------------integer = " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.d("------------onError() = "+t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("------------onSubscribe()");
            }
        };

        flowable.subscribe(subscriber);
    }

    private void useZip_1() {
        /**
         * 一是从数量上进行治理, 减少发送进水缸里的事件
         * 二是从速度上进行治理, 减缓事件发送进水缸的速度Thread.sleep(2000); || .sample(2, TimeUnit.SECONDS);
         */
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for(int i = 0; ; i++){
//                    Thread.sleep(2000);
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()).sample(2, TimeUnit.SECONDS);
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("------------s = "+s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("--------error = "+throwable.getMessage());
            }
        });
    }


    private void useZip() {
        /**
         * Zip可以将多个上游(observable)发送的事件组合起来发送给下游(observer)
         * 组合的过程是分别从 两个observable里各取出一个事件 来进行组合, 并且一个事件只能被使用一次, 组合的顺序是严格按照事件发送的顺利 来进行的,
         * 最终执行的结果 与事件最少的一个保持一致
         * onSubscribe()
         * value = 1 --> A
         * value = 2 --> B
         * value = 3 --> C
         * ---------onComplete() (数量最少的那一个observable发送的onComplete())
         */
        Observable observable1 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                e.onNext(01);
                Log.d("-----------01");
                e.onNext(02);
                Log.d("-----------02");
                e.onNext(03);
                Log.d("-----------03");
                e.onNext(04);
                Log.d("-----------04");
                e.onComplete(); // 没有得到执行
            }
        }).subscribeOn(Schedulers.io());
        Observable observable2 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                e.onNext(" --> A");
                Log.d("-----------A");
                e.onNext(" --> B");
                Log.d("-----------B");
                e.onNext(" --> C");
                Log.d("-----------C");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("---------onSubscribe()");
            }

            @Override
            public void onNext(String value) {
                Log.d("---------value = "+value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d("---------onComplete()");
            }
        });
    }

    private void useFlatMap() {
        final UserInfo[] students = getStudents();
        Observable.fromArray(students).map(new Function<UserInfo, String>() {
            @Override
            public String apply(UserInfo userInfo) throws Exception {
                return userInfo.getUserName();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("----------result = "+s);
            }
        });

        Flowable.fromArray(students).flatMap(new Function<UserInfo, Publisher<Course>>() {
            @Override
            public Publisher<Course> apply(final UserInfo userInfo) throws Exception {
                return new Publisher<Course>() {
                    @Override
                    public void subscribe(Subscriber<? super Course> s) {
                        userInfo.getCourseList();
                    }
                };
            }
        }).subscribe(new Consumer<Course>() {
            @Override
            public void accept(Course course) throws Exception {

            }
        });

        Observable.fromArray(students).flatMap(new Function<UserInfo, ObservableSource<Course>>() {
            @Override
            public ObservableSource<Course> apply(UserInfo userInfo) throws Exception {
                return Observable.fromArray(userInfo.getCourses());
            }
        });

        /**
         * 乱序
         */
        Observable.fromArray(students).flatMap(new Function<UserInfo, ObservableSource<Course>>() {
            @Override
            public ObservableSource<Course> apply(UserInfo userInfo) throws Exception {
                return Observable.fromIterable(userInfo.getCourseList());
            }
        });

        /**
         * 有序（按顺序处理数据）
         */
        Observable.fromArray(students).concatMap(new Function<UserInfo, ObservableSource<Course>>() {
            @Override
            public ObservableSource<Course> apply(UserInfo userInfo) throws Exception {
                return Observable.fromIterable(userInfo.getCourseList());
            }
        });
    }

    private UserInfo[] getStudents() {
        UserInfo[] user = new UserInfo[3];
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("aaaaaa");
        user[0] = userInfo ;
        userInfo = new UserInfo();
        userInfo.setUserName("bbbbb");
        user[1] = userInfo ;
        userInfo = new UserInfo();
        userInfo.setUserName("ccccc");
        user[2] = userInfo ;

        return user;
    }

    private void useMap() {
        int drawablesId = R.drawable.btn_mosaic_pressed;
        Observable.fromArray(drawablesId).map(new Function<Integer, Bitmap>() {
            @Override
            public Bitmap apply(Integer ids) throws Exception {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids);
                return bitmap;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    private void useScheduler() {
        Observable.just(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("---------accept = "+integer.intValue());
                    }
                });

        final int drawables = R.drawable.btn_crop_pressed;

        Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(ObservableEmitter<Drawable> e) throws Exception {
                Drawable drawable = getResources().getDrawable(drawables);
                if(drawable != null){
                    Log.d("-----------drawable !!!!= null");
                }
                e.onNext(drawable);
//                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Drawable>() {
            @Override
            public void accept(Drawable drawable) throws Exception {
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                } else {
                    Log.d("-----------drawable = null");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("---------------throwable() = "+throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.d("---------------onComplete() ");
            }
        });

    }

    private void useJustOrFrom() {
        /**
         * subscribe() 一个参数 相当于onNext()
         * 两个参数  第二个为 发生错误后调用
         * 第三个参数 相当于onComplete
         */
        Observable observable = Observable.just("李治","小欧","治治","老欧").doAfterNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("-------s = "+(s+"--> 1"));
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.d("----------onComplete()");
            }
        });
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String str) throws Exception {
                Log.d("--------accept string = " + str);
            }
        }
//          , new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                Log.d("--------accept string = " + throwable.getMessage());
//            }
//        }, new Action() {  // 重写后调用
//            @Override
//            public void run() throws Exception {
//                Log.d("--------Action run() ");
//            }
//        }
        );
        String[] str = {"李治11","小欧11","治治11","老欧11"} ;
        Observable.fromArray(str).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("----------onSubscribe()");
            }

            @Override
            public void onNext(String value) {
                Log.d("----------value = "+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("----------onError()  e = "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("----------onComplete()");
            }
        });

    }

    private void useObservable() {
        // onSubscribe() (没有被d.Disposed()) --> onNext() ...(n 个onNext) --> onComplete() 或者 onError()
        // onSubscribe() (调用d.Disposed()) 结束执行
        // onNext() 执行完后 --> 需要手动调用onComplete() or onError() ;
        // doON...() 执行对应的方法前执行，
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("Hello");
                e.onNext("DSD");
                // 一旦执行onComplete() 或者 onError() 结束onNext就不再执行
                e.onComplete();
                e.onNext("怎么样了");
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {

            }
        }).doAfterNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("-----------doAfterNext() "+("---doAfterNext ---> "+s));
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("-----------doOnNext() = "+("---doOnNext --> "+s));
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.d("-----------doOnComplete()");
            }
        }).subscribe(new Observer<String>() {
            /**
             * 最先被执行的方法，d.Disposed()判断是否被取消
             * d.Disposed() 结束任务
             * @param d
             */
            @Override
            public void onSubscribe(Disposable d) { // 最新被执行，
                Log.d("--------onSubscribe() = ");

//                d.dispose();
//                Log.d("--------onSubscribe() = "+d.isDisposed());
            }

            @Override
            public void onNext(String value) {
                Log.d("--------value = "+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("--------Throwable = "+e.getMessage());
            }
            @Override
            public void onComplete() {
                Log.d("--------onComplete()");
            }
        });
    }

    List<Bitmap> bitmapList = new ArrayList<>();
    final List<File> folders = new ArrayList<>();

    public void addImages(){
        new Thread(){
            @Override
            public void run() {
                for(File folder : folders){
                    File[] files = folder.listFiles();
                    for(File file : files){
                        if(file.getName().equals(".png")){
                            final Bitmap bitmap = getBitmapFromFile(file);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bitmapList.add(bitmap);
                                }
                            });
                        }
                    }
                }
            }
        }.start();
    }

    private Observer<String> observer = new Observer<String>() {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(String value) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    public void addBitmapUseRx(){
        Observable.fromArray(folders).flatMap(new Function<List<File>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<File> files) throws Exception {
                return null;
            }
        });

        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
            }
        });

        Observable<String> observableStr = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("");
            }
        });

        observable.subscribe(observer);

        Observable.just("",folders);

//        observable.subscribe(action);
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        };

        Consumer<Throwable> throwable = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        };

        Action complete = new Action() {
            @Override
            public void run() throws Exception {

            }
        };

        observable.subscribe(consumer);
        observable.subscribe(consumer, throwable);
        observable.subscribe(consumer, throwable, complete);
        observable.subscribe(consumer, throwable, complete, null);

        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });


    }

    private Bitmap getBitmapFromFile(File file) {
        return null ;
    }

}
