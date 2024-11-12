# JDK y Gradle
FROM gradle:7.3.0-jdk17 AS build

# directory
WORKDIR /app
COPY . /app

# Deshabilitar la supervisión de archivos en Gradle
ENV GRADLE_OPTS="-Dorg.gradle.vfs.watch=false"

# run gradle
RUN gradle clean build

# create image
FROM openjdk:17-slim
EXPOSE 8080
COPY --from=build /app/build/libs/ceis-0.0.1-SNAPSHOT.jar /app/ceis-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/ceis-0.0.1-SNAPSHOT.jar"]