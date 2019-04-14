package com.xyzbank.customerstatementprocessor.transformation;

import java.nio.file.Path;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.model.StatementRecords;
import com.xyzbank.customerstatementprocessor.util.JaxbContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * XmlFileUnmarshaller.java - Provides functionality to support XML file format.
 *
 */
@Slf4j
@Component
public class XmlFileUnmarshaller implements FileUnmarshaller {

    /**
     * Unmarshal the XML file and returns list of StatementRecord objects.
     *
     * @param Absolute path of the file
     * @return CustomerStatementRecord objects
     */
    @Override
    public List<StatementRecord> unmarshall(final Path path) {
        try {
            log.info("Transforming file {} to StatementRecords java bean", path);
            return ((StatementRecords) JaxbContextUtil.getInstance().createUnmarshaller()
                    .unmarshal(path.toFile())).getRecord();
        } catch (JAXBException exp) {
            log.error(exp.getMessage(), exp);
            throw new FileProcessingException(exp.getMessage());
        }
    }

}
