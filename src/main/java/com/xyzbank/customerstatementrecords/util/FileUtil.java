package com.xyzbank.customerstatementrecords.util;

import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_HEADERS;
import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_HEADERS_WITH_ERROR;
import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_XML;
import static com.xyzbank.customerstatementrecords.util.Constants.FILE_EXTENSION_LENGTH;
import static com.xyzbank.customerstatementrecords.util.Constants.RABOBANK_FILE_PREFIX;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.xyzbank.customerstatementrecords.exception.FileProcessingException;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil {

	//Gives the time stamp of the file
	public static String extractFileIdentifier(final Path path) {
		String fileName = path.getFileName().toString();
		if (!fileName.startsWith(RABOBANK_FILE_PREFIX)) {
			throw new FileProcessingException("Missing file prefix");
		}
		return fileName.substring(RABOBANK_FILE_PREFIX.length(),
			fileName.length() - FILE_EXTENSION_LENGTH);
	}

	//Returns list of files in the input folder
	public static List<Path> listStatementRecordFiles(final String inputFolder) {
		try (Stream<Path> files = Files.list(Paths.get(inputFolder))) {
			return files.filter(path -> path.toFile().isFile())
                        .filter(path -> path.getFileName().toString().startsWith(RABOBANK_FILE_PREFIX)
					    		&& (path.toString().endsWith(EXTENSION_CSV)
					    		|| path.toString().endsWith(EXTENSION_XML)))
					    .sorted()
				 .collect(toList());
		} catch (IOException exp) {
			log.error(exp.getMessage(), exp);
		}
		return new ArrayList<>();
	}

	// Writes file to the respective folders
	public static void writeRecordsToFile(final List<CustomerStatementRecord> records,
			final String folderPath, final String fileName, final boolean errorRecords) {
		if (records != null && !records.isEmpty()) {
            final File outputFile = new File(folderPath + File.separator + fileName + EXTENSION_CSV);
			log.info("Writing error records {}", errorRecords);
			log.info("Writing contents to file {}", outputFile);
			try {
				if (!outputFile.exists()) {
					if (errorRecords) {
                        log.info("Writing column headers {}", COLUMN_HEADERS_WITH_ERROR);
                        Files.write(outputFile.toPath(), COLUMN_HEADERS_WITH_ERROR.getBytes());
					} else {
                        log.info("Writing column headers {}", COLUMN_HEADERS);
						Files.write(outputFile.toPath(), COLUMN_HEADERS.getBytes());
					}
				}
			} catch (IOException exp) {
				log.error(exp.getMessage(), exp);
			}
			try (FileWriter writer = new FileWriter(outputFile, true)) {
				StatefulBeanToCsv<CustomerStatementRecord> beanToCsv =
						new StatefulBeanToCsvBuilder<CustomerStatementRecord>(writer)
						.withApplyQuotesToAll(false).build();
				beanToCsv.write(records);
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException exp) {
				log.error(exp.getMessage(), exp);
			}
		}
	}
}
