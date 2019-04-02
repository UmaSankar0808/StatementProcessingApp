package com.xyzbank.customerstatementrecords.transformation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementrecords.configuration.ApplicationConfig;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;
import com.xyzbank.customerstatementrecords.service.InputFieldsValidationService;

class InputFieldsValidationServiceTest {

	private InputFieldsValidationService fieldsValidationService;
	
	@BeforeEach
	void setUp() throws Exception {
		Files.createDirectories(Paths.get("test/xyzbank/error"));
		ApplicationConfig config = new ApplicationConfig();
		config.setErrorFolderPath("test/xyzbank/error");
		fieldsValidationService = new InputFieldsValidationService(config);
	}

	@AfterEach
	void tearDown() throws Exception {
		FileUtils.deleteDirectory(Paths.get("test").toFile());
	}

	@Test
	void testValidateCustomerStatementRecords() throws IOException {
		fieldsValidationService.validateCustomerStatementRecords(
				Arrays.asList(createCustomerStatementRecord()), "12345");
		String actualResultString = new String(Files.readAllBytes(
				Paths.get("test/xyzbank/error").resolve("records_12345.csv")));
		assertEquals("Reference,AccountNumber,Description,Start Balance,Mutation,End Balance,Error\n"
				+ "-1,123456,Free Text,-16.79,-10.00,-26.78,reference shouldn't be a negative number\n",
				actualResultString);
	}

	private CustomerStatementRecord createCustomerStatementRecord() {
		CustomerStatementRecord customerStatementRecord = new CustomerStatementRecord();
		customerStatementRecord.setAccountNumber("123456");
		customerStatementRecord.setDescription("Free Text");
		customerStatementRecord.setEndBalance(new BigDecimal("-26.78"));
		customerStatementRecord.setStartBalance(new BigDecimal("-16.79"));
		customerStatementRecord.setMutation(new BigDecimal("-10.00"));
		customerStatementRecord.setReference(-1L);
		return customerStatementRecord;
	}
}
