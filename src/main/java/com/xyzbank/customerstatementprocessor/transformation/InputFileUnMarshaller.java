package com.xyzbank.customerstatementprocessor.transformation;

import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_XML;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.model.StatementRecords;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InputFileUnMarshaller {

	private static JAXBContext jaxbInstance;

	static {
		try {
			jaxbInstance = JAXBContext.newInstance(StatementRecords.class);
		} catch (JAXBException exp) {
			log.error(exp.getMessage(), exp);
		}
	}

	public List<StatementRecord> tranformInputFileToBean(final Path path) {
		if (path.toString().endsWith(EXTENSION_XML)) {
			log.info("{} is an xml file", path.getFileName());
			return tranformXmlToBean(path);
		} else if (path.toString().endsWith(EXTENSION_CSV)) {
			log.info("{} is an csv file", path.getFileName());
			return tranformCsvToBean(path);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Convert the CSV file into StatementRecord objects
	 *
	 * @param  Absolute path of the file
	 * @return CustomerStatementRecord objects
	 */
	public List<StatementRecord> tranformCsvToBean(final Path path) {
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

	/**
	 * Unmarshall the XML file and return the StatementRecord objects.
	 *
	 * @param Absolute path of the file
	 * @return CustomerStatementRecord objects
	 */
	public List<StatementRecord> tranformXmlToBean(final Path path) {
		try {
			log.info("Transforming file {} to CustomerStatementRecords java pojo", path);
			return ((StatementRecords) jaxbInstance.createUnmarshaller()
					.unmarshal(path.toFile())).getRecord();
		} catch (JAXBException exp) {
			log.error(exp.getMessage(), exp);
			throw new FileProcessingException(exp.getMessage());
		}
	}

}
