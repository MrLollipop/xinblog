# 欣博客

## 是什么

xinblog（欣博客）开源项目，一个基于`Spring Boot`、`Spring Cloud`、`VUE`等技术，微服务架构，实现分布式、前后端分离的个人博客，提供个人建站技术支持。

## 项目展示

- [欣哥工作室：http://xinge.studio](http://xinge.studio/) 

## 项目结构说明

* admin-vue 管理台前端

* xinblog-vue 博客前端

* xinblog-root 
  
  * xinblog-admin 管理台微服务
  
  * xinblog-auth 权限管理微服务
  
  * xinblog-blog 博客微服务
  
  * xinblog-common 微服务通用工具包
  
  * xinblog-common-api 微服务RPC接口包
  
  * xinblog-gateway 网关微服务
  
  * xinblog-thirdparty 第三方组件微服务
  
  * xinge-generator 代码生成器

* db 数据库初始化脚本

## 项目架构图

<img title="" src="https://img1.imgtp.com/2022/08/13/EF1sJikA.jpg" alt="Xinblog（欣博客）架构图.jpg" width="767">

[点击看高清大图](https://img1.imgtp.com/2022/08/13/EF1sJikA.jpg)

## 项目使用技术栈

> 前端： `VUE` | `ElementUI` | `Nginx`
> 
> 服务端架构：基于 `Spring Cloud` 与 `Spring Cloud Alibaba` 微服务架构
> 
> 缓存：`Redis`
> 
> 持久层： `MySQL` | `AliYun OSS`
> 
> 流量监控： `Sleuth + Zipkin` | `Sentinel`
> 
> 容器化： `Docker`

## 有啥用

1. 用于微服务治理、分布式等技术的学习
2. 搭建欣哥工作室个人博客，输出知识分享。

## 项目开源地址

- Gitee：[欣博客: 基于微服务框架、前后端分离的个人博客](https://gitee.com/xinge2021/xinblog)
- GitHub：[GitHub - MrLollipop/xinblog: 基于微服务框架、前后端分离的个人博客](https://github.com/MrLollipop/xinblog)

## 项目截图

![首页截图](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E9%A6%96%E9%A1%B5-2.png)

首页截图

[(点击查看大图)](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E9%A6%96%E9%A1%B5.png)

![博客详情](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E8%AF%A6%E6%83%85-2.png)

博客详情

[(点击查看大图)](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E8%AF%A6%E6%83%85.png)

![标签分类](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E4%B8%93%E9%A2%98-2.png)

标签分类

[(点击查看大图)](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E4%B8%93%E9%A2%98.png)

![管理台登录](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E7%AE%A1%E7%90%86%E5%8F%B0%E7%99%BB%E5%BD%95-2.png)

管理台登录

[(点击查看大图)](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E7%AE%A1%E7%90%86%E5%8F%B0%E7%99%BB%E5%BD%95.png)

![博客列表](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E7%AE%A1%E7%90%86%E5%8F%B0%E5%8D%9A%E5%AE%A2%E5%88%97%E8%A1%A8-2.png)

博客列表

[(点击查看大图)](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E7%AE%A1%E7%90%86%E5%8F%B0%E5%8D%9A%E5%AE%A2%E5%88%97%E8%A1%A8.png)

![博客编辑](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E5%8D%9A%E5%AE%A2%E7%BC%96%E8%BE%91.png)

博客编辑

[(点击查看大图)](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/w002/%E5%8D%9A%E5%AE%A2%E7%BC%96%E8%BE%91.png)

## 技术交流

- 欣哥VX：XingeStudio (备注例子：杭州-Java10年-来自B站)，免费领取大厂面试资料
  
  ![程序员欣哥 个人VX](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/%E6%8E%A8%E5%B9%BF/%E6%AC%A3%E5%93%A5%E5%B7%A5%E4%BD%9C%E5%AE%A4VX-%E5%B0%8F.png)

- 程序员欣哥 学习交流、技术分享、职场互助 QQ群：882838725
  
  ![程序员欣哥 QQ群](https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/%E6%8E%A8%E5%B9%BF/%E7%A8%8B%E5%BA%8F%E5%91%98%E6%AC%A3%E5%93%A5QQ%E7%BE%A4-%E5%B0%8F.png)

- 公众号：搜一搜【程序员欣哥】

- 知乎：【程序员欣哥】
