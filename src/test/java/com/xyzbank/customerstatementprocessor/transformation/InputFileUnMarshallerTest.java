package com.xyzbank.customerstatementprocessor.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.transformation.InputFileUnMarshaller;

class InputFileUnMarshallerTest {

	private InputFileUnMarshaller transform = new InputFileUnMarshaller();

	@Test
	@DisplayName("Should transform input XML file to Java Bean")
	void testTranformInputFileToJavaBeanWithXmlInput() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_2.xml");
		List<StatementRecord> records = transform.tranformInputFileToBean(filePath);
		assertEquals(11, records.size());
	}

	@Test
	@DisplayName("Should transform input CSV file to Java Bean")
	void testTranformInputFileToCustomerStatementRecordWithCsv() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_1.csv");
		List<StatementRecord> records = transform.tranformInputFileToBean(filePath);
		assertEquals(11, records.size());
	}

	@Test
	@DisplayName("Should throw Exception for a invalid XML File")
	void testThrowExceptionForInvalidXmlFile() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_invalid.xml");
		assertThrows(FileProcessingException.class,
				() -> transform.tranformInputFileToBean(filePath));
	}

	@Test
	@DisplayName("Should throw Exception for a invalid CSV File")
	void testThrowExceptionIfInputFileNotFound() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_file_missing.csv");
		assertThrows(FileProcessingException.class,
				() -> transform.tranformInputFileToBean(filePath));
	}

	@Test
	@DisplayName("Should return an empty list of records for an invalid file extension")
	void testReturnAnEmptyListOfRecordsForInvalidFileExtension() {
		Path filePath = Paths.get("src/test/resources/").resolve("records_one.png");
		List<StatementRecord> records = transform.tranformInputFileToBean(filePath);
		assertTrue(records.isEmpty());
	}
}
