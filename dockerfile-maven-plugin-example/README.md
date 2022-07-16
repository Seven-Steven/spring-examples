# dockerfile-maven-plugin-example

## 简介

本文介绍了 [com.spotify:dockerfile-maven-plugin](https://github.com/spotify/dockerfile-maven) 的简单使用示例。
最终达成的目标是**把 docker 镜像构建**集成在 maven 打包过程中，可以使用 maven 命令构建 docker 镜像。

功能相似的插件有三个：

- [com.spotify:dockerfile-maven-plugin](https://github.com/spotify/dockerfile-maven)
  本文使用的插件，已经停止更新，但功能依旧稳定。
- ~~[com.spotify:docker-maven-plugin](https://github.com/spotify/docker-maven-plugin)~~
  本文所用插件的同胞兄弟，官方不推荐使用，已停止更新。
- [io.fabric8io:docker-maven-plugin](https://github.com/fabric8io/docker-maven-plugin)
  支持在 pom.xml 中配置 Dockerfile 的各项内容，也支持自定义 Dockerfile，支持**操作容器**，功能强大，仍在更新。

## 使用

以 spring-boot-web 项目为例：

1. 创建一个 spring-boot-web 项目并确保项目正常；
2. 依据项目需要在合适的位置定制一个 Dockerfile，这里把 Dockerfile 放在了 `src/main/docker` 目录下：

  ```dockerfile
  FROM openjdk:8-jdk-alpine
  # 设置时区
  ENV TZ Asia/Shanghai
  RUN apk --no-cache add tzdata && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
      && echo ${TZ} > /etc/timezone \
      && apk del tzdata
  
  VOLUME /tmp
  # 定义变量
  ARG JAR_FILE
  ADD target/${JAR_FILE} app.jar
  ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
  # 声明服务以 tcp 协议运行在 8080 端口
  EXPOSE 8080/tcp
  ```

3. 在 `project` 标签下添加插件配置：

  ```xml

<build>
  <plugins>
    <!--dockerfile-maven-plugin 配置-->
    <plugin>
      <groupId>com.spotify</groupId>
      <artifactId>dockerfile-maven-plugin</artifactId>
      <version>1.4.13</version>
      <executions>
        <execution>
          <id>build</id>
          <!--绑定 maven 阶段-->
          <!--install 表示运行 mvn install 时会自动执行 docker 镜像构建-->
          <!--install 也可以改成 package，运行 mvn package 时会自动执行 docker 镜像构建-->
          <phase>install</phase>
          <goals>
            <!--执行插件的目标-->
            <goal>build</goal>
          </goals>
        </execution>
      </executions>
      <configuration>
        <!--指定 docker 镜像仓库-->
        <repository>cn/diqigan/${project.artifactId}</repository>
        <!--指定 docker 镜像标签-->
        <tag>${project.version}</tag>
        <!--指定 Dockerfile 路径-->
        <dockerfile>src/main/docker/Dockerfile</dockerfile>
        <!--设置 Dockerfile 中变量-->
        <buildArgs>
          <!--对应 Dockerfile 中的 JAR_FILE 变量-->
          <JAR_FILE>${project.build.finalName}.jar</JAR_FILE>
        </buildArgs>
      </configuration>
    </plugin>
    <!--dockerfile-maven-plugin 配置-->
  </plugins>
</build>
  ```

4. 构建镜像

镜像构建有两种方式：

- 绑定 maven 阶段自动构建：

  以文中所示配置，执行 `mvn install` 指令会自动构建 docker 镜像。

- 显式运行构建指令 `mvn package dockerfile:buld` 构建 docker 镜像。

可以在 maven 控制台日志中看到 docker 镜像已经构建成功。

![dockerfile-maven-plugin-build-log](https://newbucket.s3.ladydaily.com/2022/a08fbe9792b7cbd4bd46b61b30ab45af.png)

## 后记

1. 个人不推荐在 pom.xml 中配置 Dockerfile 的各项内容，有种配置侵入的感觉，建议另写一个 Dockerfile 文件；
2. 持续集成过程中完全可以使用 docker 相关指令构建镜像，maven 插件有点画蛇添足的意思；

## 参考文档

- [官方使用文档](https://github.com/spotify/dockerfile-maven/blob/master/docs/usage.md)
- [官方使用示例](https://github.com/spotify/dockerfile-maven/tree/master/plugin/src/it)
