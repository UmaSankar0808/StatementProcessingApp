package com.xyzbank.customerstatementrecords.service;

import static com.xyzbank.customerstatementrecords.util.Constants.ERROR_PREFIX;
import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementrecords.util.Constants.EXTENSION_XML;
import static com.xyzbank.customerstatementrecords.util.Constants.RABOBANK_FILE_PREFIX;
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
	private TransformToCustomerStatementRecord transformer;

	@Scheduled(fixedDelayString = "${config.pollingInterval}")
	public void processCustomerRecords() {
		MDC.put("correlationId", UUID.randomUUID().toString());
		log.info("Starting polling on input folder {}", config.getInputFolderPath());
        List<Path> statementRecordFiles = FileUtil.listStatementRecordFiles(config.getInputFolderPath());
		log.info("Received {} file(s) processing them with a inputCoolingDelay {}",
				statementRecordFiles.size(), config.getInputCoolingOffPeriod());
		statementRecordFiles.forEach(path -> {
			List<CustomerStatementRecord> allRecords =
					transformer.tranformInputFileToCustomerStatementRecord(path);
			String fileIdentifier = FileUtil.extractFileIdentifier(path);
			List<CustomerStatementRecord> unquieTransactionReferenceRecords =
					filterDuplicateTransactionReferences(allRecords, fileIdentifier);
			log.info("Unique transaction records {}", unquieTransactionReferenceRecords);
			List<CustomerStatementRecord> finalRecords =
					validateBalance(unquieTransactionReferenceRecords, fileIdentifier);
			FileUtil.writeRecordsToFile(finalRecords, config.getProcessedFolderPath(),
					RABOBANK_FILE_PREFIX + fileIdentifier, false);
			try {
				String fileExtesion = path.getFileName().toString().endsWith(EXTENSION_CSV)
						? EXTENSION_CSV : EXTENSION_XML;
				Files.move(path, new File(config.getAuditFolderPath() + separator
						+ RABOBANK_FILE_PREFIX + fileIdentifier + fileExtesion).toPath());
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
					List<CustomerStatementRecordWithError> results =
							statementRecords.stream().map(record -> {
						CustomerStatementRecordWithError customerStatementRecordWithError =
								new CustomerStatementRecordWithError(record);
	                    customerStatementRecordWithError.setError("Duplicate transaction reference exists");
						return customerStatementRecordWithError;
					})
					.collect(toList());
					duplicateRecords.addAll(results);
				}
			});
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
				CustomerStatementRecordWithError customerStatementRecordWithError =
						new CustomerStatementRecordWithError(record);
				customerStatementRecordWithError.setError(
						"End balance doesn't match with start balance + mutation");
				invalidBalanceRecords.add(customerStatementRecordWithError);
			}
		});
		FileUtil.writeRecordsToFile(invalidBalanceRecords, config.getErrorFolderPath(),
				ERROR_PREFIX + fileIdentifier, true);
		return validRecords;
	}

}
