package com.xyzbank.customerstatementprocessor.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;

class CsvFileUnMarshallerTest {

    private CsvFileUnmarshaller csvFileUnmarshaller = new CsvFileUnmarshaller();

    @Test
    @DisplayName("Should transform input CSV file to Java Bean")
    void testTranformInputFileToCustomerStatementRecordWithCsv() {
        Path filePath = Paths.get("src/test/resources/").resolve("records_1.csv");
        List<StatementRecord> records = csvFileUnmarshaller.unmarshall(filePath);
        assertEquals(11, records.size());
    }

    
    @Test
    @DisplayName("Should throw Exception for a invalid CSV File")
    void testThrowExceptionIfInputFileNotFound() {
        Path filePath = Paths.get("src/test/resources/").resolve("records_file_missing.csv");
        assertThrows(FileProcessingException.class,
                () -> csvFileUnmarshaller.unmarshall(filePath));
    }

}
