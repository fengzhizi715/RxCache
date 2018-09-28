package com.cv4j.rxcache.strategy;

/**
 * Created by tony on 2018/9/28.
 */
public interface CacheStrategy extends ObservableStrategy,
        FlowableStrategy,
        SingleStrategy,
        CompletableStrategy,
        MaybeStrategy {

}
