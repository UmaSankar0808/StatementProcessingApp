package com.xyzbank.customerstatementprocessor.transformation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.util.Constants;

class InputFileUnmarshallerTest {

	private InputFileUnmarshaller inputFileUnmarshaller;
	private XmlFileUnmarshaller xmlFileUnmarshaller = Mockito.mock(XmlFileUnmarshaller.class);
	private CsvFileUnmarshaller csvFileUnmarshaller = Mockito.mock(CsvFileUnmarshaller.class);

	@BeforeEach
	void setup() {
	    inputFileUnmarshaller = new InputFileUnmarshaller(xmlFileUnmarshaller, csvFileUnmarshaller);
	}

	@Test
	@DisplayName("Should return XmlFileUnmarshaller for files with .xml extension")
	void shouldReturnXmlFileUnmarshallerForXmlFile() {
	    FileUnmarshaller unMarshaller = inputFileUnmarshaller.getUnmarshaller(Constants.EXTENSION_XML);
	    assertTrue(unMarshaller instanceof XmlFileUnmarshaller);
	}
	
    @Test
    @DisplayName("Should return CsvFileUnmarshaller for files with .csv extension")
    void shouldReturnCsvFileUnmarshallerForCsvFile() {
        FileUnmarshaller unMarshaller = inputFileUnmarshaller.getUnmarshaller(Constants.EXTENSION_CSV);
        assertTrue(unMarshaller instanceof CsvFileUnmarshaller);
    }
    
    @Test
    @DisplayName("Should throw file processing exception for a file with invalid extension example '.txt'")
    void shouldThrowFileProcessingExceptionForInvalidFileExtension() {
        assertThrows(FileProcessingException.class, () -> inputFileUnmarshaller.getUnmarshaller(".txt"));
        
    }
	
}
