
### 客户端需求

启动客户端生成一个客户端编码，

注意：Java版本的Client项目已经被golang开发的客户端替代！！！


将客户端编码绑定到账号下，然后在后台配置对于的路由规则。
具体参考花生壳。


### 启动命令

直接执行以下命令开始控制台方式运行。

```
./bin/bullet
```


### 后台运行方式


### docker版客户端镜像构建

1、首先使用mvn打包bullet-client.jar，拷贝到lib目录中。

-

2、构建Docker镜像。
```
docker build \
--build-arg VERSION=0.0.1 \
-t wuweiit/bullet-client:0.0.4 .
```

4、快速运行Bullet客户端容器

```
docker run --rm --name=bullet-client \
wuweiit/bullet-client:0.0.1
```

5、将Bullet配置文件映射出来，自定义配置。


```
docker run --name=bullet-client \
-v /opt/bullet/conf/config.json:/opt/bullet/conf/config.json \
-e BULLET_DEVICE_NO=e6460ae9ba154d65a26c550d6266c801 \
wuweiit/bullet-client:0.0.1
```


### docker-compose 编排

```
version: '2'
services:
    bullet-client:
      image: 'wuweiit/bullet-client:0.0.1'
      restart: always
      volumes:
        - /opt/bullet/conf/config.json:/opt/bullet/conf/config.json

```

配置文件`/opt/bullet/conf/config.json` 内容
```
{
	"deviceNo":"",
	"logService":true,
	"tunnel":"ws://joggle.cn:8081/tunnel"
}
```

配置文件`/opt/bullet/conf/ngrok.yml` 内容
```
server_addr: "joggle.cn:8083"
trust_host_root_certs: false
use_insecure_skip_verify: false
use_client_crt_path: /Users/marker/WORK/git/Bullet/Client/conf/cert/ngrokroot.crt
web_addr: false
console_ui: false

```
以上两个配置文件，改为自己的域名。

tunnel 需要自己部署服务端。



