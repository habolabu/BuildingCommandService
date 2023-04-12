FROM gradle:jdk17-alpine
WORKDIR /building-command-service

COPY src /building-command-service/src/
COPY build.gradle .
COPY lombok.config .

RUN gradle clean build -x test

ENTRYPOINT ["java", "-jar", "./build/libs/building-command-service-1.0.0.jar"]