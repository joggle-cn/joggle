FROM ascdc/jdk8:latest

# 构建参数
ARG JAR

# 工作空间
WORKDIR /app

# 开发者
MAINTAINER docker_user admin@wuweibi.com

ARG VERSION='0.0.1'
# 时区处理
ENV TZ Asia/Shanghai

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

# maven输出的jar包复制到镜像中
COPY $JAR /app/app.jar

# 日志输出目录
RUN mkdir -p /data

# 导出的端口
EXPOSE 8081

# 启动项目命令
CMD java -jar /app/app.jar