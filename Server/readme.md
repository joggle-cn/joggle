
### 启动 参数

log.dir 指定日志输出路径，默认为/var/log/bullet



### 环境变量

环境变量可以保证应用程序的配置灵活性，可以自主定义数据库以及域名。

| 变量  | 默认值  | 说明|
| ------------ | ------------ | ------------ |
| BULLET_DOMAIN  | joggle.cn  | Bullet一级域名|
| BULLET_MYSQL_HOST  | 192.168.1.104  | 数据库地址 |
| BULLET_MYSQL_PORT | 3307| 数据库端口 |
| BULLET_MYSQL_DATABASE | db_bullet| 数据库名称 |
| BULLET_MYSQL_USERNAME | root| 数据库账号 |
| BULLET_MYSQL_PASSWORD | 12| 数据库密码 |



### 域名SSL证书

证书必须使用泛*证书，可以使用多个域名共同使用的证书。

配置路径在 `/opt/bullet-server/conf/`

| 文件    | 说明|
| ------------ | ------------ |
| cert.pem | 服务端证书 |
| chain.pem | 浏览器需要的所有证书但不包括服务端证书，比如根证书和中间证书 |
| fullchain.pem | 包括了cert.pem和chain.pem的内容 |
| privkey.pem | 证书的私钥 |

Bullet 使用了fullchain.pem、privkey.pem两个文件

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