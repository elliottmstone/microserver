FROM java:8
EXPOSE 8080
ADD /target/0.0.1-SNAPSHOT-fat.jar 0.0.1-SNAPSHOT-fat.jar
ENTRYPOINT ["java","-jar","0.0.1-SNAPSHOT-fat.jar"]