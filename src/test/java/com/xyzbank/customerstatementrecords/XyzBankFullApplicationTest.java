package com.xyzbank.customerstatementrecords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class XyzBankFullApplicationTest {

	Path errorPath = Paths.get("test/xyzbank/error");
	Path inputPath = Paths.get("test/xyzbank/input");
	Path auditPath = Paths.get("test/xyzbank/audit");
	Path processedPath = Paths.get("test/xyzbank/processed");
	
	@BeforeAll
	static void setupBeforeAll() {
		System.setProperty("config.inputFolderPath", "test/xyzbank/input");
		System.setProperty("config.errorFolderPath", "test/xyzbank/error");
		System.setProperty("config.auditFolderPath", "test/xyzbank/audit");
		System.setProperty("config.processedFolderPath", "test/xyzbank/processed");
	}

	@BeforeEach
	void setup() throws IOException {
		Files.createDirectories(errorPath);
		Files.createDirectories(inputPath);
		Files.createDirectories(auditPath);
		Files.createDirectories(processedPath);
	}
	
	@Test
	@DisplayName("Tests the full functionality with csv file")
	void csvFullTest() throws IOException, InterruptedException {
		byte[] inputFileBytes = Files.readAllBytes(Paths.get("src/test/resources/").resolve("records_1.csv"));
        Files.write(inputPath.resolve("records_1.csv"), inputFileBytes);
        TimeUnit.SECONDS.sleep(11);
        //Verify error folder contents
        assertEquals(new String(Files.readAllBytes(Paths.get("src/test/resources/").resolve("errors_csv_records_1.csv"))),
        		new String(Files.readAllBytes(errorPath.resolve("records_1.csv"))));
        //Verify audit folder contents
        assertEquals(new String(inputFileBytes), new String(Files.readAllBytes(auditPath.resolve("records_1.csv"))));
        //Verify processed folder contents
        assertEquals(new String(Files.readAllBytes(Paths.get("src/test/resources/").resolve("processed_csv_records_1.csv"))),
        		new String(Files.readAllBytes(processedPath.resolve("records_1.csv"))));
        //Verify input folder shouldn't contain csv file.
        assertFalse(inputPath.resolve("records_1.csv").toFile().exists());
	}
	
	@Test
	@DisplayName("Tests the full functionality with xml file")
	void xmlFullTest() throws IOException, InterruptedException {
		byte[] inputFileBytes = Files.readAllBytes(Paths.get("src/test/resources/").resolve("records_2.xml"));
        Files.write(inputPath.resolve("records_2.xml"), inputFileBytes);
        TimeUnit.SECONDS.sleep(11);
        //Verify error folder contents
        assertEquals(new String(Files.readAllBytes(
        		Paths.get("src/test/resources/").resolve("errors_xml_records_2.csv")), StandardCharsets.UTF_8),
        		new String(Files.readAllBytes(errorPath.resolve("records_2.csv"))));
        //Verify audit folder contents
        assertEquals(new String(inputFileBytes), new String(Files.readAllBytes(auditPath.resolve("records_2.xml"))));
        //Verify processed folder contents
        assertEquals(new String(Files.readAllBytes(
        		Paths.get("src/test/resources/").resolve("processed_xml_records_2.csv")), StandardCharsets.UTF_8),
        		new String(Files.readAllBytes(processedPath.resolve("records_2.csv"))));
        //Verify input folder shouldn't contain xml file.
        assertFalse(inputPath.resolve("records_1.xml").toFile().exists());
	}

	@AfterEach
	void tearDown() throws IOException {
		FileUtils.cleanDirectory(errorPath.toFile());
		FileUtils.cleanDirectory(auditPath.toFile());
		FileUtils.cleanDirectory(processedPath.toFile());
	}
	
	@AfterAll
	static void tearDownAfterAll() {
		FileUtils.deleteQuietly(Paths.get("test").toFile());
	}
}
