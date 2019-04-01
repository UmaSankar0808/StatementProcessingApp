##Important Note:

To run this project in eclipse lombok.jar needs to be added in eclipse.ini file `-javaagent:lombok.jar`

If you are using eclipse, go to `Window -> Preferences -> General -> Workspace` and select `Unix` from the Other drop down box in `New text file line delimiter`

## Setup

1. Clone the repository.

2. If maven is installed and setup properly `mvn clean spring-boot:run`

4. If you are behind a proxy set proxy to download maven artifacts

	Windows: set MAVEN_OPTS=-Dhttps.proxyHost=proxyhost -Dhttps.proxyPort=8080

	Linux or MAC: export MAVEN_OPTS=-Dhttps.proxyHost=proxyhost -Dhttps.proxyPort=8080

## Run application
```
mvn clean spring-boot:run
```

## Package and run application
```
mvn clean package -Dmaven.test.skip
java -jar target\customerstatementrecords*.jar
```

For running tests this project uses maven defaults
 - Unit test ends with `*Test.java`

## Running check style
```
mvn clean compile jxr:jxr checkstyle:checkstyle
```

## Running tests
```
Unit tests -> mvn clean test
```