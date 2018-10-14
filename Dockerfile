FROM java:8
EXPOSE 8080
ADD /target/web.jar web.jar
ENTRYPOINT ["java","-jar","/web.jar"]