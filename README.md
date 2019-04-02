## Important Note:

To run this project in eclipse lombok.jar needs to be added in eclipse.ini file `-javaagent:lombok.jar`

## Technologies used:

* Java 8
* Spring boot - 2.1.3-RELEASE
* Open CSV - 4.5
* Junit 5 (Junit Jupiter)
* Lombok
* hibernate-validator (Java Bean Validation)
* Slf4j+Logback (Logging)
* Maven (Build Tool).

## Setup:

1. Clone the repository.
2. If maven is installed and setup properly run `mvn clean spring-boot:run`
3. If you are behind a proxy set proxy to download maven artifacts
	Windows: set MAVEN_OPTS=-Dhttps.proxyHost=proxyhost -Dhttps.proxyPort=8080
	Linux or MAC: export MAVEN_OPTS=-Dhttps.proxyHost=proxyhost -Dhttps.proxyPort=8080
	
## Assumptions:

1. Input files records.csv & records.xml already came from upstream system and readily available in the xyzbank/input folder with the naming format like records_timestamp.csv/records_timestamp.xml.
2. Time stamp is in Milliseconds.
3. Once processing is finished files from the input folder are deleted & moved to audit folder.
4. Input folder polling interval is taken as 5sec (Spring scheduler is doing the polling job).
5. Current spring profile in use is local-sandbox with logging as console Info level, for other profiles (dev, staging, production) logs will be written to /data/logs/xyzbank/application.log (with daily compression and purging after 21 days).
6. Output for both the CSV and XML files written into separate CSV files (since the output format is not specified).
7. Fork Join Pool size taken as 4 to process 4 files parallelly.

## How to see the Output:
1. Input files are already kept under the xyzbank/input folder with the above specified naming convention (e.g., records_1554098803818.csv & records_1554098803819.xml)
2. Processed files will be available in xyzbank/processed folder in CSV format with the same filename and time stamp.
3. Error records are separately moved to xyzbank/error folder.
4. A copy of all the input files are kept under xyzbank/audit folder.

## Package and run application
```
mvn clean package -Dmaven.test.skip
java -jar -Djava.util.concurrent.ForkJoinPool.common.parallelism=4 target\Customerstatementprocessing-0.0.1.jar
```

For running tests this project uses maven defaults
 - Unit test ends with `*Test.java`

## Running check style
```
mvn clean compile jxr:jxr checkstyle:checkstyle
```

## Run all unit tests
```
mvn clean test
```

## Sonarqube report
![alt text](https://github.com/UmaSankar0808/StatementProcessingApp/blob/master/Sonar_reprot.jpg)