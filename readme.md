### Bullet 反向代理内网穿透服务器

基于Java实现，通过WebSocket全双工长连接技术，通过NIO接受HTTP请求，将请求信息通过WebSocket链接发送给链接的客户端。


![image](docs/images/WX20190411-102621.png)

Ngrok强势入驻，由于Ngrok在内网穿透这块确实稳定，故不再造轮子，在轮子的基础上做了优化，支持Server端Web管理。

目前仅对Mac环境做了适配，需要其他环境替换bin/ngrok命令.

[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)

注意：拒绝996工作制公司请使用本项目。




### ngrok 编译

http://www.yl-blog.com/article/608.html

本项目需要手动编译对应平台ngrok命令替换项目中的ngrok命令。


### 编译Client & Server

编译
```
mvn install
```

进入到bullet目录运行程序
```
cd Client

./bin/bullet

```

启动客户端

```
./bin/bullet
```


### 服务端安装

服务器端需要占用一个通道端口，写死在代码中的8081端口。

1、先将数据库初始化好（见databases目录中）

2、修改Server项目中的config.properties 配置文件。

3、打war包
```
cd Server && mvn package
```

4、将war放到Tomcat容器中。

5、使用域名泛解析到服务器IP。


### Bullet 特性

1、断粮重试机制，链接断后间隔10s尝试连接；
2、真真意义的反向代理，长连接反向请求；
3、支持Http协议、TCP/IP协议