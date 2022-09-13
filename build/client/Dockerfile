FROM debian:buster-slim

# add our user and group first to make sure their IDs get assigned consistently,
# regardless of whatever dependencies get added
RUN groupadd -r -g 777 joggle && useradd -r -g joggle -u 777 joggle

RUN set -eux; \
	savedAptMark="$(apt-mark showmanual)"; \
	#apt-get update; \
	#apt-get install -y --no-install-recommends ca-certificates dirmngr gnupg wget; \
	rm -rf /var/lib/apt/lists/*; \


	echo $dpkgArch \

# 工作空间
VOLUME /opt/joggle
WORKDIR /opt/joggle

# 开发者
MAINTAINER docker_user admin@wuweibi.com

# 时区处理
ENV TZ Asia/Shanghai

# 中文乱码处理
ENV LANG C.UTF-8


# 容器内部文件处理
ADD ./bin /opt/joggle/bin/
ADD ./conf /opt/joggle/conf/
ADD ./logs /opt/joggle/logs/


RUN ls /opt/bullet/bin && \
  chown -R joggle:joggle /opt/bullet && \
  echo "hello..."


# 启动项目命令
# ENTRYPOINT ["bin/bullet.bat"]

EXPOSE 443

ENTRYPOINT ["/opt/bullet/bin/service"]