### Bullet 反向代理内网穿透服务器

基于Java实现，通过WebSocket全双工长连接技术，通过NIO接受HTTP请求，将请求信息通过WebSocket链接发送给链接的客户端。



### 客户端编译
编译公共库
```
cd Common && mvn install
```

编译客户端
```
cd Client && mvn install
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

