FROM eclipse/alpine_jdk8

# 构建参数
ENV JAVA_OPTS -Xms256m -Xmx512m
ENV TZ Asia/Shanghai
ENV APP_ENV dev
ENV SERVER_PORT 80
ENV LANG C.UTF-8

# 构建参数
ARG JAR

# 工作空间
WORKDIR /app

# 开发者
MAINTAINER docker_user admin@wuweibi.com

# Bullet设备编码
#ENV NGROKD_BIN '/opt/bullet-server/bin/ngrokd'

RUN sudo mkdir /app/logs && sudo chmod -R 777 /app/logs

# 容器内部文件处理
#ADD ../build/server/bin /opt/bullet-server/bin/
#ADD ../build/server/conf /opt/bullet-server/conf/
ADD ./target/bullet-server.jar /app/app.jar
#ADD ../build/server/logs /opt/bullet-server/logs/



# 启动命令
CMD exec $JAVA_HOME/bin/java -jar $JAVA_OPTS /app/app.jar --spring.profiles.active=$APP_ENV

