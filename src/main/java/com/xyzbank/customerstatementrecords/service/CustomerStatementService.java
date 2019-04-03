package com.xyzbank.customerstatementrecords.service;

import static com.xyzbank.customerstatementrecords.util.Constants.ERROR_PREFIX;
import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_XML;
import static com.xyzbank.customerstatementrecords.util.Constants.XYZBANK_FILE_PREFIX;
import static java.io.File.separator;
import static java.util.stream.Collectors.toList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.xyzbank.customerstatementrecords.configuration.ApplicationConfig;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecordWithError;
import com.xyzbank.customerstatementrecords.transformation.TransformToCustomerStatementRecord;
import com.xyzbank.customerstatementrecords.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerStatementService {

	private ApplicationConfig config;
	private InputFieldsValidationService fieldsValidationService;
	private TransformToCustomerStatementRecord transformer;

	@Scheduled(fixedDelayString = "${config.pollingInterval}")
	public void processCustomerRecords() {
		MDC.put("correlationId", UUID.randomUUID().toString());
		log.info("Starting polling on input folder {}", config.getInputFolderPath());

		//Get the list of files to from input folder to process.
		List<Path> statementRecordFiles =
				FileUtil.listStatementRecordFiles(config.getInputFolderPath());
		log.info("Received {} file(s)",	statementRecordFiles.size());

		//Process each file separately
		statementRecordFiles.parallelStream().forEach(path -> {
			//Get the the time stamp of each file
			String fileIdentifier = FileUtil.extractFileIdentifier(path);

			//Transform input file records into java objects.
			List<CustomerStatementRecord> allRecords =
					transformer.tranformInputFileToCustomerStatementRecord(path);
			log.info("Number of records are {} in the input file {}",
					allRecords.size(), path.getFileName());

			//Validates input data and writes error records to error file
			List<CustomerStatementRecord> validatedRecords =
			fieldsValidationService.validateCustomerStatementRecords(allRecords, fileIdentifier);

			//Filter the duplicate records which are having same Transaction Reference number.
			List<CustomerStatementRecord> unquieTransactionReferenceRecords =
			filterDuplicateTransactionReferences(validatedRecords, fileIdentifier);
			log.info("Unique number of transaction records are {}",
					unquieTransactionReferenceRecords.size());

			//Validate the Balance
			List<CustomerStatementRecord> finalRecords =
					validateBalance(unquieTransactionReferenceRecords, fileIdentifier);
			log.info("Final number of transaction records are {}", finalRecords.size());

			//Write the results in to processed folder
			FileUtil.writeRecordsToFile(finalRecords, config.getProcessedFolderPath(),
					XYZBANK_FILE_PREFIX + fileIdentifier, false);

			//Take a back up copy of incoming files into audit folder.
			try {
                String fileExtesion = path.getFileName().toString().endsWith(EXTENSION_CSV)
                		? EXTENSION_CSV : EXTENSION_XML;
				Files.move(path, new File(config.getAuditFolderPath() + separator
                      + XYZBANK_FILE_PREFIX + fileIdentifier + fileExtesion).toPath());
			} catch (IOException exp) {
				log.error(exp.getMessage(), exp);
			}
		});
	}

	protected List<CustomerStatementRecord> filterDuplicateTransactionReferences(
			final List<CustomerStatementRecord> records, final String fileIdentifier) {
		Map<Long, List<CustomerStatementRecord>> treeMap = new HashMap<>();
		List<CustomerStatementRecord> withOutDuplicateRecords = new ArrayList<>();
		List<CustomerStatementRecord> duplicateRecords = new ArrayList<>();
		records.forEach(record -> {
			List<CustomerStatementRecord> statementRecords = treeMap.get(record.getReference());
			if (statementRecords == null) {
				statementRecords = new ArrayList<>();
				treeMap.put(record.getReference(), statementRecords);
			}
			statementRecords.add(record);
		});
		treeMap.forEach((key, value) -> {
			List<CustomerStatementRecord> statementRecords = value;
			if (value.size() == 1) {
				withOutDuplicateRecords.addAll(statementRecords);
			} else {
                List<CustomerStatementRecordWithError> results = statementRecords.stream().map(record -> {
                    CustomerStatementRecordWithError recordwitherror =
                    		new CustomerStatementRecordWithError(record);
					recordwitherror.setError("Duplicate transaction reference exists");
					return recordwitherror;
				}).collect(toList());
				duplicateRecords.addAll(results);
			}
		});

		//Write duplicate records into Error file.
		FileUtil.writeRecordsToFile(duplicateRecords, config.getErrorFolderPath(),
				ERROR_PREFIX + fileIdentifier, true);
		return withOutDuplicateRecords;
	}

	protected List<CustomerStatementRecord> validateBalance(
        final List<CustomerStatementRecord> unquieTransactionReferenceRecords, final String fileIdentifier) {
		List<CustomerStatementRecord> validRecords = new ArrayList<>();
		List<CustomerStatementRecord> invalidBalanceRecords = new ArrayList<>();
		unquieTransactionReferenceRecords.forEach(record -> {
            if (record.getStartBalance().add(record.getMutation()).compareTo(record.getEndBalance()) == 0) {
				validRecords.add(record);
			} else {
                CustomerStatementRecordWithError recordwitherror =
                		new CustomerStatementRecordWithError(record);
                recordwitherror.setError("End balance doesn't match with start balance + mutation");
				invalidBalanceRecords.add(recordwitherror);
			}
		});

		//Write invalid balance records into Error file.
		FileUtil.writeRecordsToFile(invalidBalanceRecords, config.getErrorFolderPath(),
				ERROR_PREFIX + fileIdentifier, true);
		return validRecords;
	}

}
