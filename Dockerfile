FROM adoptopenjdk/openjdk11:latest
EXPOSE 8080
ARG JAR_FILE=target/server-0.0.1.jar
ADD ${JAR_FILE} server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","server.jar"]