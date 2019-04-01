package com.xyzbank.customerstatementrecords.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementrecords.configuration.ApplicationConfig;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;
import com.xyzbank.customerstatementrecords.service.CustomerStatementService;
import com.xyzbank.customerstatementrecords.transformation.TransformToCustomerStatementRecord;

class CustomerStatementServiceTest {
	
	private CustomerStatementService customerStatementService;
	private TransformToCustomerStatementRecord transformer = mock(TransformToCustomerStatementRecord.class);

	@BeforeEach
	void setUp() throws Exception {
		Files.createDirectories(Paths.get("test/xyzbank/error"));
		ApplicationConfig config = new ApplicationConfig();
		config.setErrorFolderPath("test/xyzbank/error");
		customerStatementService = new CustomerStatementService(config, transformer);
	}

	@Test
	void shouldFilterOutDuplicates() {
		CustomerStatementRecord record1 = createCustomerStatementRecord();
		CustomerStatementRecord record2 = createCustomerStatementRecord();
		CustomerStatementRecord record3 = createCustomerStatementRecord();
		record3.setReference(543543L);
		List<CustomerStatementRecord> actualResult = customerStatementService
				.filterDuplicateTransactionReferences(Arrays.asList(record1, record2, record3), "12345");
		assertEquals(Arrays.asList(record3), actualResult);
	}
	
	@Test
	void shouldFilterOutRecordWithIncorrectEndBalance() throws IOException {
		CustomerStatementRecord record = createCustomerStatementRecord();
		record.setReference(543543L);
		List<CustomerStatementRecord> actualResult = customerStatementService
				.validateBalance(Arrays.asList(record), "12345");
		assertTrue(actualResult.isEmpty());
		String actualResultString = new String(Files.readAllBytes(
				Paths.get("test/xyzbank/error").resolve("records_12345.csv")));
		assertEquals("Reference,AccountNumber,Description,Start Balance,Mutation,End Balance,Error\n" + 
				"543543,123456,Free Text,-16.79,-10.00,-26.78,"
				+ "End balance doesn't match with start balance + mutation\n", actualResultString);
	}
	
	private CustomerStatementRecord createCustomerStatementRecord() {
		CustomerStatementRecord customerStatementRecord = new CustomerStatementRecord();
		customerStatementRecord.setAccountNumber("123456");
		customerStatementRecord.setDescription("Free Text");
		customerStatementRecord.setEndBalance(new BigDecimal("-26.78"));
		customerStatementRecord.setStartBalance(new BigDecimal("-16.79"));
		customerStatementRecord.setMutation(new BigDecimal("-10.00"));
		customerStatementRecord.setReference(15455634L);
		return customerStatementRecord;
	}
	

	@AfterEach
	void tearDown() throws Exception {
		FileUtils.deleteDirectory(Paths.get("test").toFile());
	}

}
