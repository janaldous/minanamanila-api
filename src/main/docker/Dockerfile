FROM openjdk:8-jre-alpine
MAINTAINER janaldous.com
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=$PORT", "application.jar"]
