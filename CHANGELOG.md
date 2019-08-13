RxCache
===

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