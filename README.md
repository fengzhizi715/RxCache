# RxCache

RxCache 是一款支持 Java 和 Android 的 Local Cache 。目前，支持堆内存、堆外内存、磁盘缓存。

[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


# 功能特点：

* 拥有二级缓存：Memory、Persistence
* 各个缓存可以拥有有效时间，超过时间缓存会过期
* Memory 默认支持 FIFO、LRU、LFU 算法的实现
* Memory 额外支持 Guava Cache、Caffeine、MapDB 的实现
* Memory 支持显示缓存使用的统计数据。
* Memory 支持堆外内存(off-heap)
* Persistence 默认使用 gson 实现对象的序列化和反序列化
* Persistence 额外支持使用 fastjson、moshi 实现对象的序列化和反序列化
* Persistence 的 DiskImpl 拥有加密功能，默认支持 AES 128、DES 加密
* 使用 Builder 模式生成 Type
* 线程安全
* 支持 Retrofit 风格使用缓存
* 支持 RxJava 2


## 支持的 Annotation：

注解名称|作用|备注
---|-------------|-------------
@CacheClass|设置缓存类，标注一个Class对象|参数注解
@CacheKey|设置缓存的key值|方法注解
@CacheLifecycle|设置缓存的过期时间，只在缓存保存时有效|方法注解
@CacheMethod|设置缓存的操作方法。以及返回的对象是 RxJava 的各种 Observable 类型，或者返回所存储的对象类型。|方法注解
@CacheValue|设置缓存的值|参数注解

# 最新版本

模块|最新版本
---|:-------------:
rxcache-core|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-core/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-core/_latestVersion)|
rxcache-proxy|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-proxy/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-proxy/_latestVersion)|
rxcache-guava-cache|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-guava-cache/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-guava-cache/_latestVersion)|
rxcache-caffeine|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-caffeine/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-caffeine/_latestVersion)|
rxcache-fastjson|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-fastjson/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-fastjson/_latestVersion)|
rxcache-moshi|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-moshi/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-moshi/_latestVersion)|
rxcache-off-heap|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-off-heap/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-off-heap/_latestVersion)|

对于 Java 工程，如果使用 gradle 构建，由于默认没有使用 jcenter()，需要在相应 module 的 build.gradle 中配置

```groovy
repositories {
    mavenCentral()
    jcenter()
}
```

## 下载：

rxcache-core

```groovy
implementation 'com.safframework.rxcache:rxcache-core:1.2.2'
```

rxcache-proxy

```groovy
implementation 'com.safframework.rxcache:rxcache-proxy:1.2.2'
```

rxcache-guava-cache

```groovy
implementation 'com.safframework.rxcache:rxcache-guava-cache:1.2.2'
```

rxcache-caffeine

```groovy
implementation 'com.safframework.rxcache:rxcache-caffeine:1.2.2'
```

rxcache-fastjson

```groovy
implementation 'com.safframework.rxcache:rxcache-fastjson:1.2.2'
```

rxcache-moshi

```groovy
implementation 'com.safframework.rxcache:rxcache-moshi:1.2.2'
```

rxcache-off-heap

```groovy
implementation 'com.safframework.rxcache:rxcache-off-heap:1.2.2'
```

# 详细功能查看[wiki](https://github.com/fengzhizi715/RxCache/wiki)

# 感谢

* 参考了[RxCache](https://github.com/VictorAlbertos/RxCache)的实现
* 参考了[RxCache](https://github.com/z-chu/RxCache)的实现
* 参考了[TypeBuilder](https://github.com/ikidou/TypeBuilder)的实现


联系方式
===

Wechat：fengzhizi715


> Java与Android技术栈：每周更新推送原创技术文章，欢迎扫描下方的公众号二维码并关注，期待与您的共同成长和进步。

![](https://user-gold-cdn.xitu.io/2018/7/24/164cc729c7c69ac1?w=344&h=344&f=jpeg&s=9082)

License
-------

    Copyright (C) 2018 - present, Tony Shen.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
