### Bullet 内网穿透Web管理工具

本项目基于Java实现，是一款通过Bullet协议控制Ngrok客户端，实现的Web远程管理工具。


### Bullet 特性

- 快速、稳定；
- 断连重试机制；
- 反向代理请求；
- 支持Http、Https协议、TCP协议；
- TLS实现数据的安全传输；

![image](docs/images/WX20191226-100852.jpg)
 
目前实现了Mac、linux、window全环境适配.

[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
 

### Bullet 逻辑架构


![image](docs/images/WX20190603-173120.png)

### Bullet 项目结构


|目录 |说明|
|:---- |----   |
| bullet-common  | 公共模块，包含加密、日志配置、Bullet协议、Builer结构 |
| bullet-client | Bullet客户端代理程序，主要控制Ngrok客户端协同操作| 
| bullet-server | Server端为服务端主控程序，提供系列接口供Web前端页面调用，控制Client操作Ngrok客户端| 
| bullet-codemaker | 本人比较懒，用的Mybatis-Plus生成代码| 

Bullet占用的端口说明
|端口 |说明|
|:---- |----   |
| 8083 | Ngrok通道 |
| 80   | 代理的http端口 | 
| 443  | 代理的https端口 | 
| 8081 | Bullet WEB管理服务（默认端口，可通过环境变量修改) |   


### Bullet Server 部署

bullet的部署非常简单，百度网盘下载对应的server包，解压。

- 1、安装mysql数据库并执行源码中的sql文件；
- 2、下载bullet-server的部署并解压；
- 3、配置bin/bullet-server脚本的环境变量;
- 4、执行./bin/bullet-server启动;
- 5、访问http://localhost:8081

 