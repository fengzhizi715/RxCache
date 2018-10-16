# RxCache

支持 Java 和 Android 的 Local Cache 。

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


# RxCache 的设计：

RxCache 包含了两级缓存： Memory 和 Persistence 。

![](images/rxcache_uml.png)



联系方式
===

Wechat：fengzhizi715


> Java与Android技术栈：每周更新推送原创技术文章，欢迎扫描下方的公众号二维码并关注，期待与您的共同成长和进步。

![](https://user-gold-cdn.xitu.io/2018/7/24/164cc729c7c69ac1?w=344&h=344&f=jpeg&s=9082)

License
-------

    Copyright (C) 2017 Tony Shen.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.