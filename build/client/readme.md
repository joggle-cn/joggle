### 构建命令


```
docker build \
--build-arg VERSION=0.0.4 \
-t wuweiit/bullet-client:0.0.4 .
```

--platform=linux/amd64,linux/arm64

推送镜像到仓库

```
# 登录dockerhub
docker login

# 推送镜像
docker push wuweiit/bullet-client:0.0.4
```
