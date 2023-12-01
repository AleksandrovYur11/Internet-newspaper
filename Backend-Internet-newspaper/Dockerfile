FROM openjdk:8

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл приложения из локальной директории в контейнер
COPY target/Backend-Internet-newspaper-0.0.1-SNAPSHOT.jar application.jar

# Определяем точку входа для контейнера (запускаем JAR-файл приложения)
ENTRYPOINT ["java", "-jar", "application.jar"]



