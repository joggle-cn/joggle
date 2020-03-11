
### 启动 参数

log.dir 指定日志输出路径，默认为/var/log/bullet





### docker版客户端镜像构建

1、首先使用mvn打包bullet-server.jar。



2、构建Docker镜像。
```
docker build \
--build-arg VERSION=0.0.1 \
-t wuweiit/bullet-server:0.0.1 .
```

4、快速运行Bullet客户端容器

```
docker run --rm --name=bullet-server \ 
wuweiit/bullet-server:0.0.1
```

5、将Bullet配置文件映射出来，自定义配置。


```
docker run --name=bullet-server \ 
wuweiit/bullet-server:0.0.1
```