
### 构建镜像


```
docker build \
--build-arg VERSION=0.0.1.release \
-t wuweiit/springboot:0.0.1 .
```



### docker-compose 快速启动

```
docker network create app_net
```

```
docker-compose up -d

```