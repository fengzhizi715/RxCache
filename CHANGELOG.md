RxCache
===

Version 2.1.9
---
2023-10-9
* 完善 RxCache 的 checkKey() 函数
* 升级 Kotlin 版本到 1.8

Version 2.1.8
---
2023-2-14
* 第三方库的升级
* 增加对外暴露的日志接口

Version 2.1.7
---
2023-1-16
* 提供校验缓存 key 的功能
* 升级 Kotlin 版本到 1.7

Version 2.1.6
---
2022-5-9
* 优化 Disk 接口相关的实现类

Version 2.1.4
---
2021-11-3
* 优化 Persistence 接口
* 增加 Chronicle-Map 模块

Version 2.1.3
---
2021-10-24
* 增加 ohc 模块

Version 2.1.2
---
2021-10-21
* caffeine 升级

Version 2.1.1
---
2021-10-10
* Kotlin 升级到 1.5.31
* 第三方库的升级
* 优化底层

Version 2.0.1
---
2021-02-22
* 优化代码逻辑以及 Memory 中生成 CacheStatistics 的方式

Version 2.0.0
---
2021-02-19
* RxCache 不再依赖 RxJava 3.x，可以支持 RxJava 2.x 也可以支持 RxJava 3.x

Version 1.8.4
---
2021-02-13
* Kotlin 版本升级到1.4.30
* 增加缓存 key 同步删除、异步删除的策略

Version 1.8.3
---
2020-10-02
* Kotlin 版本升级到1.4.10

Version 1.8.2
---
2020-09-15
* Kotlin 版本升级到1.4.0

Version 1.8.1
---
2020-07-17
* 增加 rxcache-extension-coroutines 模块
* 增加 rxcache-extension-result 模块

Version 1.8.0
---
2020-07-16
* RxCache 增加 saveOrUpdate()

Version 1.7.4
---
2020-07-05
* 完善 RxCache 的 info()

Version 1.7.3
---
2020-07-04
* 增加 rxcache-persistence-mapdb 模块

Version 1.7.2
---
2020-07-03
* RxCache 增加 config() 

Version 1.7.1
---
2020-07-02
* RxCache+Extension 增加 memory()、persistence() 

Version 1.7.0
---
2020-07-01
* RxCache 增加 getStringData()、parseStringData() 

Version 1.6.6
---
2020-06-27
* 优化 ProtobufConverter，core 模块增加 TypeUtils

Version 1.6.5
---
2020-06-24
* 优化 ProtobufConverter

Version 1.6.4
---
2020-06-23
* RxCache 增加 saveMemory(), 用于保存缓存并且只存于内存中。

Version 1.6.3
---
2020-06-22
* 增加 rxcache-converter-protobuf 模块

Version 1.6.1
---
2020-06-16
* 更新部分 Converters

Version 1.6.0
---
2020-06-15
* 升级 RxJava 的版本，使用 RxJava 3.x

Version 1.5.1
---
2020-06-14
* 更新所依赖的第三方项目的版本

Version 1.5.0
---
2020-02-10
* 重构RxCache的load相关的方法

Version 1.4.7
---
2020-02-09
* 更新所依赖的第三方项目的版本，升级gradle版本

Version 1.4.6
---
2019-10-05
* 完善 mapdb 模块

Version 1.4.5
---
2019-10-03
* 更新所依赖的第三方项目的版本

Version 1.4.4
---
2019-07-01
* 完善 extension 模块

Version 1.4.3
---
2019-07-01
* 修复 extension 模块的bug

Version 1.4.2
---
2019-06-26
* 增加 extension 模块
* 更新所依赖的第三方项目的版本

Version 1.4.0
---
2019-04-13
* 调整项目结构
* 更新所依赖的第三方项目的版本

Version 1.3.2
---
2019-02-15
* RxCache 增加 expire 方法
* RxCache 的 save、update、expire 方法增加 TimeUnit 参数

Version 1.3.1
---
2019-02-11
* Memory 接口去掉 default 方法

Version 1.3.0
---
2019-02-10
* 修复 MoshiConverter 的bug
* 增加缓存的info()方法，用于显示缓存中的信息。
* 使用 Builder 模式生成 Type

Version 1.2.0
---
2019-02-03
* 升级Kotlin版本
* 增加堆外内存的支持
* 增加MapDB的支持
* 使用bytekit替换原先的tony-common-utils

Version 1.1.0
---
2018-12-28
* 增加 Annotation，类似 Retrofit 风格的方式，支持通过标注 Annotation 来完成缓存的操作。

Version 1.0.0
---
2018-11-06
* 初始化项目，大体完成RxCache，并初步可以使用。