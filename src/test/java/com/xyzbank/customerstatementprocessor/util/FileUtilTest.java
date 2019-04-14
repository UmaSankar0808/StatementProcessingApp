package com.xyzbank.customerstatementprocessor.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;

class FileUtilTest {
	
	@BeforeEach
	void setup() throws IOException {
		Files.createDirectories(Paths.get("test/xyzbank/input"));
	}
	
	@Test
	@DisplayName("Should extract the file identifier from file name ex:- records_123456.csv file identfier is 123456")
	void testExtractFileIdentifier() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_123456.csv"), new String("test").getBytes());
		String actualResult = FileUtil.extractFileIdentifier(
				Paths.get("test/xyzbank/input").resolve("records_123456.csv"));
		assertEquals("123456", actualResult);
	}
	
	@Test
	@DisplayName("Should throw file processing exception when the input file name"
	        + "doesn't match with format records_(identifier).xml/csv")
	void shouldThrowFileProcessingExceptionForIncorrectFileName() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("recor_123456.csv"), new String("test").getBytes());
		assertThrows(FileProcessingException.class, () -> FileUtil.extractFileIdentifier(
				Paths.get("test/xyzbank/input").resolve("recor_123456.csv")));
	}

	@Test
	@DisplayName("Should return an empty list when input folder doesn't exists")
	void testListInputFilesWithInvalidInputFolder() {
		List<Path> statementRecordFiles = FileUtil.getListOfFiles("invalidinputfolder");
		assertTrue(statementRecordFiles.isEmpty());
	}
	
	@Test
	@DisplayName("Should return an empty list if input file naming doesn't starts with records_ and ends with .csv or .xml")
	void testListInputFilesStartWithAndEndsWith() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("invalid_1.csv"), new String("test").getBytes());
		Files.write(Paths.get("test/xyzbank/input").resolve("invalid_1.xml"), new String("test").getBytes());
		List<Path> statementRecordFiles = FileUtil.getListOfFiles("test/xyzbank/input");
		assertTrue(statementRecordFiles.isEmpty());
	}
	
	@Test
	@DisplayName("Should return a list of single file when a valid csv file is placed in input folder")
	void testGetListOfInputFilesWithOneValidSingleCsvFile() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_1.csv"), new String("test").getBytes());
		List<Path> inputFilesList = FileUtil.getListOfFiles("test/xyzbank/input");
		assertEquals(1, inputFilesList.size());
		assertEquals("records_1.csv", inputFilesList.get(0).getFileName().toString());
	}
	
	@Test
	@DisplayName("Should return a list of single file when a valid xml file is placed in input folder")
	void tesGettListInputFilesWithOneValidSingleXmlFile() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_1.xml"), new String("test").getBytes());
		List<Path> inputFilesList = FileUtil.getListOfFiles("test/xyzbank/input");
		assertEquals(1, inputFilesList.size());
		assertEquals("records_1.xml", inputFilesList.get(0).getFileName().toString());
	}
	
	@Test
	@DisplayName("Should return a list of two files when a valid xml file & csv are placed in input folder")
	void testGetListInputFilesWithOneXmlFileAndCsvFile() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_1.xml"), new String("test").getBytes());
		Files.write(Paths.get("test/xyzbank/input").resolve("records_2.csv"), new String("test").getBytes());
		List<Path> inputFiles = FileUtil.getListOfFiles("test/xyzbank/input");
		assertEquals(2, inputFiles.size());
		assertEquals("records_1.xml", inputFiles.get(0).getFileName().toString());
		assertEquals("records_2.csv", inputFiles.get(1).getFileName().toString());
	}
	
	@Test
	@DisplayName("Should write/marshall the StatementRecords(Java Beans) to a csv file")
	void testWriteRecordsToFile() throws IOException {
		FileUtil.writeToCsvFile(Arrays.asList(StatementRecordBuilder.buildStatementRecord()),
				Paths.get("test/xyzbank/input").toAbsolutePath().toString(), "test");
		String actualResult = new String(Files.readAllBytes(Paths.get("test/xyzbank/input/").resolve("test.csv")));
		assertEquals("Reference,AccountNumber,Description,Start Balance,Mutation,End Balance,Error\n".toUpperCase() + 
					 "15455634,123456,Free Text,-16.78,-10.00,-26.78,\n", actualResult);
	}
	
	@Test
	@DisplayName("Should write/marshall the StatementRecords(Java Beans) to a csv file with error column")
	void testWriteRecordsToFileWithError() throws IOException {
		StatementRecord statementRecordWithError = StatementRecordBuilder.buildStatementRecord();
		statementRecordWithError.setError("TestError");
		FileUtil.writeToCsvFile(Arrays.asList(statementRecordWithError),
				Paths.get("test/xyzbank/input").toAbsolutePath().toString(), "test");
		String actualResult = new String(Files.readAllBytes(Paths.get("test/xyzbank/input/").resolve("test.csv")));
		assertEquals("Reference,AccountNumber,Description,Start Balance,Mutation,End Balance,Error\n".toUpperCase() + 
					 "15455634,123456,Free Text,-16.78,-10.00,-26.78,TestError\n", actualResult);
	}

	@AfterEach
	void tearDown() throws IOException {
		FileUtils.deleteDirectory(Paths.get("test").toFile());
	}

}