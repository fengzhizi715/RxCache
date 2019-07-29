package proxy;

import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.proxy.MethodType;
import com.safframework.rxcache.proxy.annotation.*;
import domain.User;
import io.reactivex.Maybe;

/**
 * Created by tony on 2018/10/31.
 */
public interface Provider {

    @CacheKey("user")
    @CacheMethod(methodType = MethodType.GET)
    <T> Record<T> getData(@CacheClass Class<T> clazz);


    @CacheKey("user")
    @CacheMethod(methodType = MethodType.SAVE)
    @CacheLifecycle(duration = 2000)
    void putData(@CacheValue User user);


    @CacheKey("user")
    @CacheMethod(methodType = MethodType.REMOVE)
    void removeUser();

    @CacheKey("test")
    @CacheMethod(methodType = MethodType.GET)
    <T> Maybe<Record<T>> getMaybe(@CacheClass Class<T> clazz);
}
