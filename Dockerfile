FROM nexus.mrcms.cn:8083/wuweiit/alpine_jdk8:0.0.1

# 构建参数
ARG JAR

# 工作空间
WORKDIR /app

# 开发者
MAINTAINER docker_user admin@wuweibi.com


ENV JVM_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs/oom/heapdump.hprof -XX:OnOutOfMemoryError='/app/script/heapDump.sh'
ENV JAVA_OPTS -Xms256m -Xmx512m -XX:MaxMetaspaceSize=300m
# 时区处理
ENV TZ Asia/Shanghai
# 中文乱码处理
ENV LANG C.UTF-8



ARG VERSION='0.0.1'


# maven输出的jar包复制到镜像中
COPY $JAR /app/app.jar

# 日志输出目录
RUN sudo mkdir -p /data

# 导出的端口
EXPOSE 8081


#利用ENTRYPOINT一定会执行的特点，将它作为PID=1托管进程
ENTRYPOINT ["/sbin/tini","--"]

# 启动命令
CMD exec $JAVA_HOME/bin/java $SKYWALKING_OPTS ${SENTINEL_OPTS} -Dspring.output.ansi.enabled=ALWAYS \
    -Dsecurerandom.source=file:/dev/urandom -Drocketmq.client.logUseSlf4j=true \
    -server -jar $JVM_OPTS $JAVA_OPTS $JAVA_JMX_OPTS /app/app.jar \
    --spring.profiles.active=$APP_ENV $JAVA_PARAMS

