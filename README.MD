## Доступные команды:

- POST /bankaccount/{id}\
    Запрос:\
    Ответ:
    ```
        {
            "bankAccountId": 12345,
            "sum": 500
        }
    ```​
- PUT /bankaccount/{id}/deposit\
    Запрос:
    ```
        {
            "sum": 500
        }
    ```
    Ответ:
    ```
        {
            "bankAccountId": 12345,
            "sum": 500
        }
    ```
- PUT /bankaccount/{id}/withdraw\
    Запрос:
    ```
        {
            "sum": 500
        }
    ```
    Ответ:
    ```
        {
            "bankAccountId": 12345,
            "sum": 500
        }
    ```
- GET /bankaccount/{id}/balance\
    Запрос:\
    Ответ:
    ```
        {
            "bankAccountId": 12345,
            "sum": 500
        }
    ```
## Сборка и запуск:

- Запуск PostgreSQL и приложения в Docker
```
    docker-compose up
```
- Остановка и удаление PostgreSQL и приложения в Docker
```
    docker-compose down
    docker rmi mq-camel-course
    docker rmi postgres-camel-course
```
- Запуск тестов (включает запуск в Docker, инициацию тестов, остановку)
```
    runTestDocker.sh
```
- Docker image проекта:
```
    mvn clean package -Dmaven.test.skip=true
    docker-compose build