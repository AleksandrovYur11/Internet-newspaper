#FROM openjdk:8
#FROM arm64v8/ubuntu:latest
#
## Указываем рабочую директорию внутри контейнера
#WORKDIR /app
#
## Копируем JAR-файл приложения из локальной директории в контейнер
#COPY target/Backend-Internet-newspaper-0.0.1-SNAPSHOT.jar application.jar
#
## Определяем точку входа для контейнера (запускаем JAR-файл приложения)
#ENTRYPOINT ["java", "-jar", "application.jar"]

# Указываем базовый образ Ubuntu для архитектуры arm64
FROM arm64v8/ubuntu:latest

# Обновляем пакетный менеджер и устанавливаем OpenJDK 8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean;

# Определяем переменную окружения JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-arm64

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл вашего приложения из локальной директории в контейнер
COPY target/Backend-Internet-newspaper-0.0.1-SNAPSHOT.jar /app/application.jar

# Определяем точку входа для контейнера (запуск Java-приложения)
CMD ["java", "-jar", "application.jar"]



