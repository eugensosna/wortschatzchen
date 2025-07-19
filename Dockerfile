FROM gradle:jdk21 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon


FROM openjdk:21 
EXPOSE 8080

RUN mkdir /app

# ENV SPRING_PROFILES_ACTIVE=dev

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions",  "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar", "$JAVA_OPTS"]