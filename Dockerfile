FROM redspark/oracle-jdk-8:latest

# 构建参数
ARG JAR

# 工作空间
WORKDIR /app

# 开发者
MAINTAINER docker_user admin@wuweibi.com

ARG VERSION='0.0.1'
# 时区处理
ENV TZ Asia/Shanghai

# 中文乱码处理
ENV LANG C.UTF-8

# maven输出的jar包复制到镜像中
COPY $JAR /app/app.jar

# 日志输出目录
RUN mkdir -p /data

# 导出的端口
EXPOSE 8081

# 启动项目命令
CMD java -jar /app/app.jar --spring.profiles.active=$APP_ENV