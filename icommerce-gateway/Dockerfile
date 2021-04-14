FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8020
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]