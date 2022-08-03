FROM java:8

MAINTAINER haoxin<haoxin_2014@163.com>

#定义环境变量
ARG JAR_FILE
ARG APP_NAME=xinblog-thirdparty
#ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
#-Duser.timezone=GMT+8 -Dlog4j2.formatMsgNoLookups=true -XX:MetaspaceSize=256m   -XX:MaxMetaspaceSize=256m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled  -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump/${APP_NAME}.dump

#设置工作目录
WORKDIR /app

#复制文件到容器目录
ADD ${JAR_FILE}  /app.jar

#定义匿名数据卷
VOLUME ["/usr/local/${APP_NAME}/logs"]

#配置容器启动后执行的命令
#ENTRYPOINT exec java ${JAVA_OPTS}  ${APP_OPTS}  -jar /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
