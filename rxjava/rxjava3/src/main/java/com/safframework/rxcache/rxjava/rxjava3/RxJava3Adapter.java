package com.safframework.rxcache.rxjava.rxjava3;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.adapter.Adapter;
import com.safframework.rxcache.key.KeyThreadFactory;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @FileName: com.safframework.rxcache.rxjava3.RxJava3Adapter
 * @author: Tony Shen
 * @date: 2021-02-19 01:42
 * @version: V1.0 <描述当前版本功能>
 */
public class RxJava3Adapter implements Adapter {

    private Disposable disposable;

    @Override
    public void interval(RxCache rxCache) {
        disposable = Observable.interval(10, 7200, TimeUnit.SECONDS) // 每隔2小时，清理过期的缓存
                .observeOn(Schedulers.from(Executors.newSingleThreadExecutor(new KeyThreadFactory())))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {

                        rxCache.getEvictionPool().forEach(new BiConsumer<String, Type>() {
                            @Override
                            public void accept(String s, Type type) {
                                long ttl = rxCache.ttl(s, type);
                                if (ttl == 0) {
                                    rxCache.remove(s);
                                }
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        System.out.println(throwable.getMessage());
                    }
                });
    }

    @Override
    public void dispose() {
        if (disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
