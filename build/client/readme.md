## Joggle客户端说明

Joggle客户端是基于ngrok1.x的优化版本，新增了P2P、UDP、https vhost等功能的支持。

主要特性：

- 支持http+https、Http、Https协议、TCP协议、P2P协议；
- 免费开源、灵活配置、多租户支持、多协议支持；
- TLS实现数据的安全传输；
- 断连重试机制；
- 反向代理请求（请合法使用）；
- 远程网络唤醒（v1.2.3开始支持）；
- 支持多CPU平台docker容器镜像；
- 任意门支持，提供文件传输服务 （v1.2.15开始支持）
- 支持P2P（NAT打洞)管理，不消耗公网付费流量；（v1.2.15开始支持）

注意：请合法使用joggle平台，发现违法使用永久封停账号！！！

## 快速开始运行客户端

基于命令行跑joggle客户端，跑起来后，在joggle平台绑定设备，然后创建映射就行了。

直接运行service命令即可，输出日志：
```
服务运行...
2022/09/13 13:18:08 [PID]  9716
```

## 客户端自动启动

如果你不满足于使用黑色窗口形式，joggle支持在window linux mac下注册为服务。

```
# cd 到程序的home目录

# 注册服务

# windows 下注册为服务
./bin/service.exe install

# linux & mac 下注册为服务
./bin/service install

service joggle start


# 卸载服务

# windows 下卸载服务
./bin/service.exe uninstall

# linux & mac 下注册为服务
./bin/service uninstall

service joggle stop
```
注册服务会配置自动启动，在windows下，注册成功后需要手动在服务中点击启动。
如果你使用的linux，那么默认 会配置自动启动，同样的也需要使用service或者systemctl命令启动


### 构建为Docker镜像 

进入到程序的home目录，然后执行docker build 构建镜像
```
docker build \
--build-arg VERSION=0.0.1 \
-t wuweiit/joggle:0.0.1 .
```

构建成功后，使用docker run 启动或者自行配置docker-compose或k8s启动。

```
docker run --rm wuweibi/joggle
```

**注意**：如果你构建的版本过低，在run起来的时候，客户端会在5小时候后做一次更新，更新会自动重启。


**推送镜像到仓库**

```
# 登录dockerhub
docker login

# 推送镜像
docker push wuweiit/joggle:0.0.1
``` 
 


**基于buildx 构建多平台镜像**

```
docker buildx build -t wuweiit/bullet1 \
--platform=linux/amd64 .
```
--platform=linux/amd64,linux/arm64
