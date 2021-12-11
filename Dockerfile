FROM openjdk:14
ADD target/spring-essentials.jar spring-essentials.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spring-essentials.jar"]