nohup docker-compose up
mvn clean test
docker-compose down
docker rmi mq-camel-course
docker rmi postgres-camel-course