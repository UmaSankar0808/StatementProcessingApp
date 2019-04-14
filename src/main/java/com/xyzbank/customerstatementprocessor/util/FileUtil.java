package com.xyzbank.customerstatementprocessor.util;

import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_XML;
import static com.xyzbank.customerstatementprocessor.util.Constants.FILE_EXTENSION_LENGTH;
import static com.xyzbank.customerstatementprocessor.util.Constants.XYZBANK_FILE_PREFIX;
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
import com.xyzbank.customerstatementprocessor.exception.FileProcessingException;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil {

	/**
	 * Extracts the timestamp of the file from the filename.
	 *
	 * @param path Absolute path of the file
	 * @return the extracted timestamp from filename
	*/
	public static String extractFileIdentifier(final Path path) {
		String fileName = path.getFileName().toString();
		if (!fileName.startsWith(XYZBANK_FILE_PREFIX)) {
			throw new FileProcessingException("Missing file prefix");
		}
		return fileName.substring(XYZBANK_FILE_PREFIX.length(),
			fileName.length() - FILE_EXTENSION_LENGTH);
	}

	/**
	 * Returns list of files in the input folder
	 *
	 * @param inputFolder Absolute path of the input folder
	 * @return Returns list of files
	 */
	public static List<Path> getListOfFiles(final String inputFolder) {
		try (Stream<Path> files = Files.list(Paths.get(inputFolder))) {
			return files.filter(path -> path.toFile().isFile())
                        .filter(path -> path.getFileName().toString().startsWith(XYZBANK_FILE_PREFIX)
					    		&& (path.toString().endsWith(EXTENSION_CSV)
					    		|| path.toString().endsWith(EXTENSION_XML)))
					    .sorted()
				 .collect(toList());
		} catch (IOException exp) {
			log.error(exp.getMessage(), exp);
		}
		return new ArrayList<>();
	}

	/**
	 * Writes file to the respective folders
	 *
	 * @param records List of customer statement records
	 * @param folderPath to write the files
	 * @param fileName to write
	 * @param errorRecords (true/false)
	 */
	public static void writeRecordsToFile(final List<StatementRecord> records,
			final String folderPath, final String fileName) {
		if (records != null && !records.isEmpty()) {
            final File outputFile = new File(folderPath + File.separator + fileName + EXTENSION_CSV);
			log.info("Writing contents to file {}", outputFile);
			try (FileWriter writer = new FileWriter(outputFile, true)) {
				StatefulBeanToCsv<StatementRecord> beanToCsv =
						new StatefulBeanToCsvBuilder<StatementRecord>(writer)
						.withMappingStrategy(CsvHeaderColumnNameMappingStrategy.getInstance())
						.withApplyQuotesToAll(false)
						.withOrderedResults(true).build();
				beanToCsv.write(records);
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException exp) {
				log.error(exp.getMessage(), exp);
			}
		}
	}
}
