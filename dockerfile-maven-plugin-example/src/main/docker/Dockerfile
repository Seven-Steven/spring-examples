FROM openjdk:8-jdk-alpine
# 设置时区
ENV TZ Asia/Shanghai
RUN apk --no-cache add tzdata && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && apk del tzdata

VOLUME /tmp
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
# 声明服务以 tcp 协议运行在 8080 端口
EXPOSE 8080/tcp
