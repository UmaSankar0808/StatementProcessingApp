package com.xyzbank.customerstatementrecords.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementrecords.exception.FileProcessingException;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;

class TransformToCustomerStatementRecordTest {
	
	TransformToCustomerStatementRecord transform = new TransformToCustomerStatementRecord();
	
	@Test
	void testTranformInputFileToCustomerStatementRecordWithXml() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_2.xml");
		List<CustomerStatementRecord> records = transform.tranformInputFileToCustomerStatementRecord(filePath);
		assertEquals(11, records.size());
	}

	@Test
	void testTranformInputFileToCustomerStatementRecordWithCsv() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_1.csv");
		List<CustomerStatementRecord> records = transform.tranformInputFileToCustomerStatementRecord(filePath);
		assertEquals(11, records.size());
	}
	
	@Test
	void shouldThrowExceptionForInvalidXmlFile() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_invalid.xml");
		assertThrows(FileProcessingException.class,
				() -> transform.tranformInputFileToCustomerStatementRecord(filePath));
	}
	
	@Test
	void shouldThrowExceptionIfInputFileNotFound() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_file_missing.csv");
		assertThrows(FileProcessingException.class,
				() -> transform.tranformInputFileToCustomerStatementRecord(filePath));
	}
	
	@Test
	void shouldReturnAnEmptyListOfRecordsForInvalidFileExtension() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_one.png");
		List<CustomerStatementRecord> records = transform.tranformInputFileToCustomerStatementRecord(filePath);
		assertTrue(records.isEmpty());
	}
}
