#  小象电商 B2B2C多用户商城系统

#### 介绍
小象电商是采用JAVA开发的B2B2C多用户商城系统。以“平台自营+多商户入驻”为主要经营模式，可快速帮客户打造类似“京东”一样的自营+招商入驻的经营模式电商平台。覆盖微信小程序、PC、H5、APP，涵盖直播、分销等社交电商能力。 产品核心经过百万级电商项目考验，安全、稳定、功能扩展强，全面满足企业电商平台业务长线、快速发展的需求。


#### 官网
官网地址：https://www.xiaoxiangai.com
技术交流QQ群：958957921


#### 演示环境
1. PC和H5商城：https://weixin.xiaoxiangai.com 测试账户：jack/123456
2. 商家连接：https://weixin.xiaoxiangai.com/business  测试账户：business/123456
3. 总后台：https://weixin.xiaoxiangai.com/admin 测试账户：admin/123456


#### 软件架构
1. 核心框架：Spring Boot 2.0.3.RELEASE
2. 安全框架：Apache Shiro 1.4.0
3. 视图框架：Spring MVC 5.0.6
4. 搜索框架：Elasticsearch 6.7.0
5. 任务调度：Spring + Quartz 2.2.3
6. 持久层框架：MyBatis 3.4.6 + Mybatis-plus 2.3
7. 数据库连接池：Alibaba Druid 1.1.10
8. 缓存框架：Ehcache 2.6 + Redis 3.2
9. 日志管理：SLF4J 1.7 + Log4j2 2.7
10. 工具类：Apache Commons、Jackson 2.9.6、fastjson 1.2.6


#### 推荐运行环境
1. 操作系统：Linux、Unix、Windows
2. JDK：JDK 1.8
3. 应用服务器：Tomcat 7.0
4. 数据库：MySQL 5.6 +
5. Redis：3.2
6. Elasticsearch：6.7.0


#### 安装教程

1. 环境准备


- 确保您的电脑已安装JDK 1.8,Eclipse或别的开发工具；
- 确保你的电脑上已经下载安装MySQL 5.6+
- 安装elasticsearch-6.7.0。常见错误帮助（https://www.cnblogs.com/yijialong/p/9707238.html）：

启动elasticsearch 报如下错误：
[2017-05-10T10:04:50,648][WARN ][o.e.b.JNANatives         ] unable to install syscall filter: 
java.lang.UnsupportedOperationException: seccomp unavailable: CONFIG_SECCOMP not compiled into kernel, CONFIG_SECCOMP and CONFIG_SECCOMP_FILTER are needed
解决方法：
修改elasticsearch.yml 添加一下内容
bootstrap.memory_lock: false
bootstrap.system_call_filter: false

2. 运行mysql文件

- 开源版本运行free_db.sql； 
- 旗舰版本运行flagship_db.sql；


3. 使用IED打开java项目，Maven拉去需要的jar包；
    也可以去百度网盘下载jar包，放到自己的仓库，链接：https://pan.baidu.com/s/1pkw1ZgrYBqKRxdJv_V6Chw；提取码：4321 

4. 修改application.yml

- 文件地址：/src/main/resources/application.yml;
- 配置当前使用的运行环境：profiles；
- 在对应的运行环境文件中（application-test、application-dev、application-pro），配置数据库、redis、elasticsearch地址。


5. 启动服务

- 文件地址：/src/main/java/net/xiaoxiangshop/ApplicationB2B2C.java
- 右击 -> Run As -> Java Application


#### 使用说明
[小象电商产品手册](https://www.yuque.com/xiaoxiangai/b2b2c/)



#### 用户权益

- 允许免费用于学习、毕设等。
- 代码文件需保留相关license信息。
- 禁止直接将本项目挂淘宝等商业平台出售。
- 禁止基于本项目直接进行商业项目和获利的相关行为。
- 非界面代码50%以上相似度的二次开源，二次开源需先联系作者。




#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
