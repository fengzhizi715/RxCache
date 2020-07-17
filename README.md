# RxCache

RxCache 是一款支持 Java 和 Android 的 Local Cache 。目前支持内存、堆外内存、磁盘缓存。

[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/78ffe7c5da004d82a48280aca9f50f42)](https://app.codacy.com/app/fengzhizi715/RxCache?utm_source=github.com&utm_medium=referral&utm_content=fengzhizi715/RxCache&utm_campaign=Badge_Grade_Dashboard)


# 功能特点：

* 支持二级缓存：Memory、Persistence
* 各个缓存可以拥有有效时间，超过时间缓存会过期
* Memory 默认支持 FIFO、LRU、LFU 算法的实现
* Memory 额外支持 Guava Cache、Caffeine、MapDB 的实现
* Memory 支持堆外内存(off-heap)
* Persistence 默认使用 Gson 实现对象的序列化和反序列化
* Persistence 额外支持使用 FastJSON、Moshi、Kryo、Hessian、FST、Protobuf 实现对象的序列化和反序列化
* Persistence 的 AbstractConverter 拥有加密功能，默认使用 AES 128、DES 算法进行加密
* 支持显示缓存的信息，包括 Memory 缓存使用的统计数据，Persistence 使用的类型和 Converter 的类型
* 支持 Kotlin, 特别是使用 kotlin extension 模块，可以规避范型擦除
* 使用 Builder 模式生成复杂对象的 Type
* 线程安全
* 支持 RxJava 2、RxJava 3
* 支持 Retrofit 风格使用缓存

## 更详细的功能请查看[wiki](https://github.com/fengzhizi715/RxCache/wiki)

## Tips

RxCache 1.6.0 以及之后的版本使用 RxJava 3.x

RxCache 1.5.1 以及之前的版本使用 RxJava 2.x

# 最新版本

模块|最新版本
---|:-------------:
rxcache-core|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-core/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-core/_latestVersion)|
rxcache-proxy|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-proxy/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-proxy/_latestVersion)|
rxcache-extension|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-extension/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-extension/_latestVersion)|
rxcache-memory-guava-cache|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-memory-guava-cache/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-memory-guava-cache/_latestVersion)|
rxcache-memory-caffeine|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-memory-caffeine/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-memory-caffeine/_latestVersion)|
rxcache-memory-off-heap|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-memory-off-heap/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-memory-off-heap/_latestVersion)|
rxcache-memory-mapdb|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-memory-mapdb/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-memory-mapdb/_latestVersion)|
rxcache-converter-fastjson|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-converter-fastjson/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-converter-fastjson/_latestVersion)|
rxcache-converter-moshi|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-converter-moshi/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-converter-moshi/_latestVersion)|
rxcache-converter-kryo|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-converter-kryo/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-converter-kryo/_latestVersion)|
rxcache-converter-hessian|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-converter-hessian/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-converter-hessian/_latestVersion)|
rxcache-converter-fst|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-converter-fst/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-converter-fst/_latestVersion)|
rxcache-converter-protobuf|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-converter-protobuf/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-converter-protobuf/_latestVersion)|
rxcache-persistence-okio|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-persistence-okio/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-persistence-okio/_latestVersion)|
rxcache-persistence-mapdb|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-persistence-mapdb/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-persistence-mapdb/_latestVersion)|
rxcache-persistence-diskmap|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/rxcache-persistence-diskmap/images/download.svg) ](https://bintray.com/fengzhizi715/maven/rxcache-persistence-diskmap/_latestVersion)|

对于 Java 工程，如果使用 gradle 构建，由于默认没有使用 jcenter()，需要在相应 module 的 build.gradle 中配置

```groovy
repositories {
    mavenCentral()
    jcenter()
}
```

# [下载](https://github.com/fengzhizi715/RxCache/blob/master/Download.md)


![](images/RxCache.png)

# TODO List:

* 增加 jmh 作为基准测试


# 感谢

* 参考了[RxCache](https://github.com/VictorAlbertos/RxCache)的实现
* 参考了[RxCache](https://github.com/z-chu/RxCache)的实现
* 参考了[TypeBuilder](https://github.com/ikidou/TypeBuilder)的实现


# Contributors：

* [snailflying](https://github.com/snailflying)


联系方式
===

Wechat：fengzhizi715


> Java与Android技术栈：每周更新推送原创技术文章，欢迎扫描下方的公众号二维码并关注，期待与您的共同成长和进步。

![](https://github.com/fengzhizi715/NetDiscovery/blob/master/images/gzh.jpeg)

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
