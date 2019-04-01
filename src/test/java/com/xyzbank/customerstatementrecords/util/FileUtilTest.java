package com.xyzbank.customerstatementrecords.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
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

import com.xyzbank.customerstatementrecords.exception.FileProcessingException;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecordWithError;

class FileUtilTest {
	
	@BeforeEach
	void setup() throws IOException {
		Files.createDirectories(Paths.get("test/xyzbank/input"));
	}
	
	@Test
	@DisplayName("Should extract the file identifier from file name")
	void testExtractFileIdentifier() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_123456.csv"), new String("test").getBytes());
		String actualResult = FileUtil.extractFileIdentifier(
				Paths.get("test/xyzbank/input").resolve("records_123456.csv"));
		assertEquals("123456", actualResult);
	}
	
	@Test
	@DisplayName("Should throw file processing exception when file identifier is missing")
	void testExtractFileIdentifierMissing() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("recor_123456.csv"), new String("test").getBytes());
		assertThrows(FileProcessingException.class, () -> FileUtil.extractFileIdentifier(
				Paths.get("test/xyzbank/input").resolve("recor_123456.csv")));
	}

	@Test
	@DisplayName("Should return an empty list when input folder doesn't exists")
	void testListStatementRecordFilesWithInvalidFolder() {
		List<Path> statementRecordFiles = FileUtil.listStatementRecordFiles("invalidinputfolder");
		assertTrue(statementRecordFiles.isEmpty());
	}
	
	@Test
	@DisplayName("Should return an empty list if file naming doesn't starts with records_ and ends with .csv or .xml")
	void testListStatementRecordFilesStartWithAndEndsWith() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("recor_1.csv"), new String("test").getBytes());
		Files.write(Paths.get("test/xyzbank/input").resolve("recor_1.xml"), new String("test").getBytes());
		List<Path> statementRecordFiles = FileUtil.listStatementRecordFiles("test/xyzbank/input");
		assertTrue(statementRecordFiles.isEmpty());
	}
	
	@Test
	@DisplayName("Should return an single file for valid csv file")
	void testListStatementRecordFilesWithOneSingleCsvFile() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_1.csv"), new String("test").getBytes());
		List<Path> statementRecordFiles = FileUtil.listStatementRecordFiles("test/xyzbank/input");
		assertEquals(1, statementRecordFiles.size());
		assertEquals("records_1.csv", statementRecordFiles.get(0).getFileName().toString());
	}
	
	@Test
	@DisplayName("Should return an single file for valid xml file")
	void testListStatementRecordFilesWithOneSingleXmlFile() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_1.xml"), new String("test").getBytes());
		List<Path> statementRecordFiles = FileUtil.listStatementRecordFiles("test/xyzbank/input");
		assertEquals(1, statementRecordFiles.size());
		assertEquals("records_1.xml", statementRecordFiles.get(0).getFileName().toString());
	}
	
	@Test
	@DisplayName("Should return two files for valid xml & csv file")
	void testListStatementRecordFilesWithOneXmlFileAndCsvFile() throws IOException {
		Files.write(Paths.get("test/xyzbank/input").resolve("records_1.xml"), new String("test").getBytes());
		Files.write(Paths.get("test/xyzbank/input").resolve("records_2.csv"), new String("test").getBytes());
		List<Path> statementRecordFiles = FileUtil.listStatementRecordFiles("test/xyzbank/input");
		assertEquals(2, statementRecordFiles.size());
		assertEquals("records_1.xml", statementRecordFiles.get(0).getFileName().toString());
		assertEquals("records_2.csv", statementRecordFiles.get(1).getFileName().toString());
	}
	
	@Test
	@DisplayName("Should write the CustomerStatementRecords to a csv file")
	void testWriteRecordsToFile() throws IOException {
		FileUtil.writeRecordsToFile(Arrays.asList(createCustomerStatementRecord()),
				Paths.get("test/xyzbank/input").toAbsolutePath().toString(), "test", false);
		String actualResult = new String(Files.readAllBytes(Paths.get("test/xyzbank/input/").resolve("test.csv")));
		assertEquals("Reference,AccountNumber,Description,Start Balance,Mutation,End Balance\n" + 
					 "15455634,123456,Free Text,-16.78,-10.00,-26.78\n", actualResult);
	}
	
	@Test
	@DisplayName("Should write the CustomerStatementRecords to a csv file with error")
	void testWriteRecordsToFileWithError() throws IOException {
		CustomerStatementRecord customerStatementRecord = createCustomerStatementRecord();
		CustomerStatementRecordWithError customerStatementRecordWithError =
				new CustomerStatementRecordWithError(customerStatementRecord);
		customerStatementRecordWithError.setError("TestError");
		FileUtil.writeRecordsToFile(Arrays.asList(customerStatementRecordWithError),
				Paths.get("test/xyzbank/input").toAbsolutePath().toString(), "test", true);
		String actualResult = new String(Files.readAllBytes(Paths.get("test/xyzbank/input/").resolve("test.csv")));
		assertEquals("Reference,AccountNumber,Description,Start Balance,Mutation,End Balance,Error\n" + 
					 "15455634,123456,Free Text,-16.78,-10.00,-26.78,TestError\n", actualResult);
	}
	
	private CustomerStatementRecord createCustomerStatementRecord() {
		CustomerStatementRecord customerStatementRecord = new CustomerStatementRecord();
		customerStatementRecord.setAccountNumber("123456");
		customerStatementRecord.setDescription("Free Text");
		customerStatementRecord.setEndBalance(new BigDecimal("-26.78"));
		customerStatementRecord.setStartBalance(new BigDecimal("-16.78"));
		customerStatementRecord.setMutation(new BigDecimal("-10.00"));
		customerStatementRecord.setReference(15455634L);
		return customerStatementRecord;
	}

	@AfterEach
	void tearDown() throws IOException {
		FileUtils.deleteDirectory(Paths.get("test").toFile());
	}

}