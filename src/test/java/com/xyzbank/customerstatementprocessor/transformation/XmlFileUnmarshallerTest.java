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

class XmlFileUnmarshallerTest {

    private XmlFileUnmarshaller xmlFileUnmarshaller = new XmlFileUnmarshaller();

    @Test
    @DisplayName("Should transform input XML file to Java Bean")
    void testTranformInputFileToJavaBeanWithXmlInput() {
        Path filePath = Paths.get("src/test/resources/").resolve("records_2.xml");
        List<StatementRecord> records = xmlFileUnmarshaller.unmarshall(filePath);
        assertEquals(11, records.size());
    }

    @Test
    @DisplayName("Should throw Exception for a invalid XML File")
    void testThrowExceptionForInvalidXmlFile() {
        Path filePath = Paths.get("src/test/resources/").resolve("records_invalid.xml");
        assertThrows(FileProcessingException.class,
                () -> xmlFileUnmarshaller.unmarshall(filePath));
    }

}
