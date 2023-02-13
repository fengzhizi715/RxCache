package com.safframework.rxcache.log;

/**
 * @FileName: com.safframework.rxcache.log.Logger
 * @author: Tony Shen
 * @date: 2023/2/13 3:52 PM
 * @version: V1.0 <描述当前版本功能>
 */
public interface Logger {

    void i(String msg, String tag);

    void v(String msg, String tag);

    void d(String msg, String tag);

    void w(String msg, String tag, Throwable tr);

    void e(String msg, String tag, Throwable tr);
}
