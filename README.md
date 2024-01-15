# Internet-newspaper

# Запуск проекта "Internet-newspaper" с использованием Docker Compose.

## Данный проект испоьзует технологию Docker Compose, для его запуска необходимо запустить Docker и выполнить следующие шаги:

### Шаг 1: Скачивание архива проекта

```bash
curl -LJO https://github.com/AleksandrovYur11/Internet-newspaper/archive/refs/heads/master.tar.gz
```

### Шаг 2: Разархивирование папки 'docker-compose'

```bash
tar -xzvf Internet-newspaper-master.tar.gz --strip-components=2 Internet-newspaper-master/docker-compose
```

### Шаг 3: Удаление архива

```bash
rm -rf Internet-newspaper-master.tar.gz
```

### Шаг 4: Запуск приложения "Internet-newspaper"

```bash
docker-compose up
```

## В данном приложении есть 2 роли: АDMIN и USER. Назначение роли ADMIN происходит строго через БД, а USER через регистрацию.

## В приложении уже добавлены новости и пользователи.

## Email: aleksandrov@yandex.ru password: Aleksandrov47 - ADMIN

## Email: sokolova@yandex.ru password: Sokolova92 - USER

## Email: denisov@yandex.ru password: Denisov198 - USER
## Схема Базы данных:
<img width="815" alt="Internet-newspaper_DB" src="https://github.com/AleksandrovYur11/Internet-newspaper/assets/107943033/9d785046-c090-4d17-b91f-713a7546096d">

