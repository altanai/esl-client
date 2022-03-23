#
# Build stage
#
# FROM maven:3.6.0-jdk-11-slim AS build
# COPY src /home/altanai/freeswitchworkspace/src
# COPY pom.xml /home/altanai/freeswitchworkspace
# RUN mvn -f /home/altanai/freeswitchworkspace/pom.xml clean package

#
# Package stage
#
FROM java:8
# COPY --from=build /home/altanai/freeswitchworkspace/JavaESL_Freeswitch/target/JavaESL_Freeswitch-1.0-SNAPSHOT.jar /usr/local/lib/JavaESL_Freeswitch-1.0-SNAPSHOT.jar
ADD /target/JavaESL_Freeswitch-1.0-SNAPSHOT.jar JavaESL_Freeswitch-1.0-SNAPSHOT.jar
EXPOSE 8021 8084
ENTRYPOINT ["java","-jar","JavaESL_Freeswitch-1.0-SNAPSHOT.jar"]