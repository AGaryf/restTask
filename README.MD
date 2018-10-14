mvn clean package -Dmaven.test.skip=true


docker-compose build
docker-compose up &







docker build -f Dockerfile -t web .
docker build -f docker/postgres/Dockerfile -t postgres .

docker-compose -f docker-compose.yml up
docker run -p 8080:8080 -t web
docker run -p 5433:5432 -t postgres