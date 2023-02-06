FROM amazoncorretto:17

MAINTAINER "Abdullah Sayed Ahmed Sallam"

EXPOSE 5151
EXPOSE 80
EXPOSE 443

COPY target/*.jar hemmah-backend.jar

ENTRYPOINT ["java", "-jar", "/hemmah-backend.jar"]
