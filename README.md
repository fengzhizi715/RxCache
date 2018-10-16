# RxCache

支持 Java 和 Android 的 Local Cache。

[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


# 最新版本

模块|rxcache-core|rxcache-extra
---|:-------------:|:-------------:
最新版本|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-core/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-core/_latestVersion)|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-extra/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-extra/_latestVersion)


# 下载：

rxcache-core

```groovy
implementation 'com.safframework.rxcache:rxcache-core:0.4.4'
```

rxcache-extra

```groovy
implementation 'com.safframework.rxcache:rxcache-extra:0.4.4'
```


# 功能特点：

* 二级缓存：Memory、Persistence
* 各个缓存拥有有效时间，超过时间缓存会过期
* Persistence 默认使用 gson 实现对象的序列化和反序列化。
* Persistence 的缓存拥有加密功能，默认支持 AES 128、DES 加密。
* Memory 额外支持 Guava Cache、Caffeine 的实现
* 支持 RxJava 2


![](images/rxcache_uml.png)


详见：https://www.jianshu.com/p/bd924b182bcb