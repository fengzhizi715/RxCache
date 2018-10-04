package com.safframework.rxcache.exception;

/**
 * Created by tony on 2018/10/4.
 */
public class RxCacheException extends RuntimeException {

    private static final long serialVersionUID = 7513569727468490289L;

    public RxCacheException(String message) {
        super(message);
    }

    public RxCacheException(Throwable throwable) {
        super(throwable);
    }

    public RxCacheException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
