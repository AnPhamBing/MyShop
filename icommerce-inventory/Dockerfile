FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8060
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]