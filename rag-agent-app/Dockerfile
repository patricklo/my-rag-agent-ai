# 基础镜像
FROM eclipse-temurin:17-jdk

# 作者
MAINTAINER Patrick

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD target/my-rag-agent-ai-app.jar /my-rag-agent-ai-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /my-rag-agent-ai-app.jar $PARAMS"]