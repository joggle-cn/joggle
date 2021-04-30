FROM debian:buster-slim

# add our user and group first to make sure their IDs get assigned consistently,
# regardless of whatever dependencies get added
RUN groupadd -r -g 777 bullet && useradd -r -g bullet -u 777 bullet

RUN set -eux; \
	savedAptMark="$(apt-mark showmanual)"; \
	#apt-get update; \
	#apt-get install -y --no-install-recommends ca-certificates dirmngr gnupg wget; \
	rm -rf /var/lib/apt/lists/*; \


	echo $dpkgArch \

# 工作空间
VOLUME /opt/bullet
WORKDIR /opt/bullet

# 开发者
MAINTAINER docker_user admin@wuweibi.com

ARG VERSION=0.0.1
# 时区处理
ENV TZ Asia/Shanghai
# Bullet设备编码
ENV BULLET_DEVICE_NO ''

# 中文乱码处理
ENV LANG C.UTF-8


# 容器内部文件处理
ADD ./bin /opt/bullet/bin/
ADD ./conf /opt/bullet/conf/
ADD ./logs /opt/bullet/logs/


#RUN wget -O bin/ngrok  https://open.joggle.cn/ngrok/darwin_amd64/ngrok && \
RUN ls /opt/bullet/bin && \
  chown -R bullet:bullet /opt/bullet && \
  echo "hello..."


# 启动项目命令
# ENTRYPOINT ["bin/bullet.bat"]

EXPOSE 443

ENTRYPOINT ["/opt/bullet/bin/bullet"]