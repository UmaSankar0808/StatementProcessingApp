package com.xyzbank.customerstatementprocessor.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.InputFileValidationService;
import com.xyzbank.customerstatementprocessor.service.impl.InputFileValidationServiceImpl;
import com.xyzbank.customerstatementprocessor.util.StatementRecordBuilder;

class InputFileValidationServiceTest {

	private InputFileValidationService fileValidationService = new InputFileValidationServiceImpl();

	@Test
	@DisplayName("Should validate and return error message for invalid transaction reference")
	void shouldReturnErrorMessageForInvalidTransactionReference() {
	    StatementRecord statementRecord = StatementRecordBuilder.buildStatementRecord();
	    //Setting invalid transaction reference negative value
	    statementRecord.setReference(-1L);
	    final List<StatementRecord> invalidRecords = new ArrayList<>();
		List<StatementRecord> validRecords = fileValidationService.validateStatementRecords(
				Arrays.asList(statementRecord), invalidRecords);
		assertTrue(validRecords.isEmpty());
		assertEquals("reference shouldn't be a negative number", invalidRecords.get(0).getError());
	}

	@Test
    @DisplayName("Should validate and return error message for invalid start balance")
    void shouldReturnErrorMessageForInvalidStartBalance() {
        StatementRecord statementRecord = StatementRecordBuilder.buildStatementRecord();
        //Setting invalid start balance with fraction more than 2 decimal places
        statementRecord.setStartBalance(new BigDecimal("11.3213213"));
        final List<StatementRecord> invalidRecords = new ArrayList<>();
        List<StatementRecord> validRecords = fileValidationService.validateStatementRecords(
                Arrays.asList(statementRecord), invalidRecords);
        assertTrue(validRecords.isEmpty());
        assertEquals("startBalance is invalid", invalidRecords.get(0).getError());
    }

	@Test
	@DisplayName("Should validate and return error message for invalid mutation")
	void shouldReturnErrorMessageForInvalidMutation() {
	    StatementRecord statementRecord = StatementRecordBuilder.buildStatementRecord();
	  //Setting invalid mutation with fraction more than 2 decimal places 
	    statementRecord.setMutation(new BigDecimal("11.3213213"));
	    final List<StatementRecord> invalidRecords = new ArrayList<>();
	    List<StatementRecord> validRecords = fileValidationService.validateStatementRecords(
	            Arrays.asList(statementRecord), invalidRecords);
	    assertTrue(validRecords.isEmpty());
	    assertEquals("mutation is invalid", invalidRecords.get(0).getError());
	}
	
	@Test
    @DisplayName("Should validate and return error message for invalid end balance")
    void shouldReturnErrorMessageForInvalidEndBalance() {
        StatementRecord statementRecord = StatementRecordBuilder.buildStatementRecord();
      //Setting invalid mutation with fraction more than 2 decimal places 
        statementRecord.setEndBalance(new BigDecimal("11.3213213"));
        final List<StatementRecord> invalidRecords = new ArrayList<>();
        List<StatementRecord> validRecords = fileValidationService.validateStatementRecords(
                Arrays.asList(statementRecord), invalidRecords);
        assertTrue(validRecords.isEmpty());
        assertEquals("endBalance is invalid", invalidRecords.get(0).getError());
    }
}
