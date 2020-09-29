FROM amazoncorretto:8
COPY target/m-tracker-sso-*.jar /opt/m-tracker-sso.jar
EXPOSE 8080/tcp
CMD ["java", "-jar", "/opt/m-tracker-sso.jar"]
