FROM ascdc/jdk8:latest

# 工作空间
WORKDIR /opt/bullet-server

# 开发者
MAINTAINER docker_user admin@wuweibi.com

ARG VERSION='0.0.1'
# 时区处理
ENV TZ Asia/Shanghai
# Bullet设备编码
ENV NGROKD_BIN '/opt/bullet-server/bin/ngrokd'

# 邮箱配置
ENV BULLET_MAIL_USERNAME notice@wuweibi.com
ENV BULLET_MAIL_PASSWORD 123456

# 数据库配置
ENV BULLET_DOMAIN joggle.cn
ENV BULLET_MYSQL_HOST 192.168.1.104
ENV BULLET_MYSQL_PORT 3307
ENV BULLET_MYSQL_DATABASE db_bullet
ENV BULLET_MYSQL_USERNAME root
ENV BULLET_MYSQL_PASSWORD 123


# 中文乱码处理
ENV LANG C.UTF-8


# 容器内部文件处理
ADD ../build/server/bin /opt/bullet-server/bin/
ADD ../build/server/conf /opt/bullet-server/conf/
ADD ./target/bullet-server.jar /opt/bullet-server/lib/bullet-server.jar
ADD ../build/server/logs /opt/bullet-server/logs/

# 导出的端口
EXPOSE 8083
EXPOSE 8081
EXPOSE 80
EXPOSE 443

# 启动项目命令
ENTRYPOINT ["/opt/bullet-server/bin/bullet-server"]
