package com.xyzbank.customerstatementprocessor.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.InputFileValidationService;
import com.xyzbank.customerstatementprocessor.service.impl.InputFileValidationServiceImpl;
import com.xyzbank.customerstatementprocessor.util.StatementRecordBuilder;

class InputFileValidationServiceTest {

	private InputFileValidationService fileValidationService = new InputFileValidationServiceImpl();

	@Test
	void testValidateCustomerStatementRecords() {
	    StatementRecord statementRecord = StatementRecordBuilder.buildStatementRecord();
	    statementRecord.setReference(-1L);
	    final List<StatementRecord> invalidRecords = new ArrayList<>();
		List<StatementRecord> actualResult = fileValidationService.validateStatementRecords(
				Arrays.asList(statementRecord), invalidRecords);
		assertTrue(actualResult.isEmpty());
		assertEquals("reference shouldn't be a negative number", invalidRecords.get(0).getError());
	}

}
