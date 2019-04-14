package com.xyzbank.customerstatementprocessor.transformation;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsvFileUnmarshaller implements FileUnmarshaller {

    /**
     * Convert the CSV file into list of StatementRecord objects
     *
     * @param  Absolute path of the file
     * @return CustomerStatementRecord objects
     */
    @Override
    public List<StatementRecord> unmarshall(final Path path) {
        log.info("Transforming file {} to CustomerStatementRecords java pojo", path);
        String fileContents;
        try {
            fileContents = new String(Files.readAllBytes(path));
        } catch (IOException exp) {
            log.error(exp.getMessage(), exp);
            throw new FileProcessingException(exp.getMessage());
        }
        return new CsvToBeanBuilder<StatementRecord>(
                new StringReader(fileContents))
                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                    .withFilter(line -> !(line.length == 1 && line[0] == null))
                    .withType(StatementRecord.class).build().parse();
    }

}
