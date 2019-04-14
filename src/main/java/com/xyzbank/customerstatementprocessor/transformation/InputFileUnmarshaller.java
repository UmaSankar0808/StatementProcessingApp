package com.xyzbank.customerstatementprocessor.transformation;

import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_XML;

import org.springframework.stereotype.Component;

import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;

import lombok.AllArgsConstructor;

/**
 * InputFileUnmarshaller.java - This class helps in extending the functionality to JSON or other file formats in future.
 *
 */
@Component
@AllArgsConstructor
public class InputFileUnmarshaller {

    private XmlFileUnmarshaller xmlFileUnmarshaller;
    private CsvFileUnmarshaller csvFileUnmarshaller;

    /**
     * Gives the file extension type through switch-case
     * @param fileExtension
     * @return FileUnmarshaller object
     */
    public FileUnmarshaller getUnmarshaller(final String fileExtension) {
        switch (fileExtension) {
            case EXTENSION_XML:
                return xmlFileUnmarshaller;
            case EXTENSION_CSV:
                return csvFileUnmarshaller;
            default:
                throw new FileProcessingException(
                        "No sutibale unmarshaller available for the given file extension");
        }
    }
}
