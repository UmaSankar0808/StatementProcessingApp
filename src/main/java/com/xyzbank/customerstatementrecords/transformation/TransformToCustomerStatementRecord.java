package com.xyzbank.customerstatementrecords.transformation;

import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_XML;
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
import com.xyzbank.customerstatementrecords.exception.FileProcessingException;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecords;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class TransformToCustomerStatementRecord {

	private static JAXBContext jaxbInstance;

	static {
		try {
			jaxbInstance = JAXBContext.newInstance("com.xyzbank.customerstatementrecords.model");
		} catch (JAXBException exp) {
			log.error(exp.getMessage(), exp);
		}
	}

	public List<CustomerStatementRecord> tranformInputFileToCustomerStatementRecord(final Path path) {
		if (path.toString().endsWith(EXTENSION_XML)) {
			log.info("{} is an xml file", path.getFileName());
			return tranformXmlToCustomerStatementRecord(path);
		} else if (path.toString().endsWith(EXTENSION_CSV)) {
			log.info("{} is an csv file", path.getFileName());
			return tranformCsvToCustomerStatementRecord(path);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Convert the CSV file into CustomerStatementRecord objects
	 *
	 * @param  Absolute path of the file
	 * @return CustomerStatementRecord objects
	 */
	public List<CustomerStatementRecord> tranformCsvToCustomerStatementRecord(final Path path) {
		log.info("Transforming file {} to CustomerStatementRecords java pojo", path);
		String fileContents;
		try {
			fileContents = new String(Files.readAllBytes(path));
		} catch (IOException exp) {
			log.error(exp.getMessage(), exp);
			throw new FileProcessingException(exp.getMessage());
		}
		return new CsvToBeanBuilder<CustomerStatementRecord>(
		        new StringReader(fileContents))
					.withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
					.withSkipLines(1)
					.withFilter(line -> !(line.length == 1 && line[0] == null))
					.withType(CustomerStatementRecord.class).build().parse();
	}

	/**
	 * Unmarshall the XML file and return the CustomerStatementRecord objects.
	 *
	 * @param paAbsolute path of the fileth
	 * @return CustomerStatementRecord objects
	 */
	public List<CustomerStatementRecord> tranformXmlToCustomerStatementRecord(final Path path) {
		try {
			log.info("Transforming file {} to CustomerStatementRecords java pojo", path);
			return ((CustomerStatementRecords) jaxbInstance.createUnmarshaller()
					.unmarshal(path.toFile())).getRecord();
		} catch (JAXBException exp) {
			log.error(exp.getMessage(), exp);
			throw new FileProcessingException(exp.getMessage());
		}
	}

}
