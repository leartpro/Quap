FROM openjdk:16
ADD DesktopApp/target/DesktopApp-1.0-SNAPSHOT.jar DesktopApp-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "--module-path \<PATH_TO_YOUR_JAVA_FX_SDK>\javafx-sdk-17.0.0.1\lib", "--add-modules=javafx.controls,javafx.fxml", "-jar","DesktopApp-1.0-SNAPSHOT.jar"]
EXPOSE 8192