FROM ascdc/jdk8:latest

# 构建参数
ARG JAR

# 工作空间
WORKDIR /opt/bullet-server

# 开发者
MAINTAINER docker_user admin@wuweibi.com

ARG VERSION='0.0.1'
# 时区处理
ENV TZ Asia/Shanghai
# Bullet设备编码
ENV NGROKD_BIN '/opt/bullet-server/bin/ngrokd'

# 中文乱码处理
ENV LANG C.UTF-8


# 容器内部文件处理
ADD ../build/server/bin /opt/bullet-server/bin/
ADD ../build/server/conf /opt/bullet-server/conf/
ADD ./target/bullet-server.jar /opt/bullet-server/lib/bullet-server.jar
ADD ../build/server/logs /opt/bullet-server/logs/

# 导出的端口
EXPOSE 8081

# 启动项目命令
ENTRYPOINT ["/opt/bullet-server/bin/bullet-server"]
