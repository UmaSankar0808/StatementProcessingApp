package com.xyzbank.customerstatementprocessor.service.impl;

import static com.xyzbank.customerstatementprocessor.util.Constants.ERROR_PREFIX;
import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_CSV;
import static com.xyzbank.customerstatementprocessor.util.Constants.EXTENSION_XML;
import static com.xyzbank.customerstatementprocessor.util.Constants.XYZBANK_FILE_PREFIX;
import static java.io.File.separator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.xyzbank.customerstatementprocessor.configuration.ApplicationConfig;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.CustomerStatementService;
import com.xyzbank.customerstatementprocessor.service.DuplicateRecordsService;
import com.xyzbank.customerstatementprocessor.service.InputFileValidationService;
import com.xyzbank.customerstatementprocessor.service.StatementBalanceService;
import com.xyzbank.customerstatementprocessor.transformation.InputFileUnmarshaller;
import com.xyzbank.customerstatementprocessor.util.FileUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerStatementServiceImpl implements CustomerStatementService {

	private ApplicationConfig config;
	private InputFileValidationService fileValidationService;
	private InputFileUnmarshaller inputFileUnmarshaller;
	private StatementBalanceService statementBalanceService;
	private DuplicateRecordsService duplicateRecordsService;

	@Scheduled(fixedDelayString = "${config.pollingInterval}")
	public void processCustomerRecords() {
		MDC.put("correlationId", UUID.randomUUID().toString());
		log.info("Starting polling on input folder {}", config.getInputFolderPath());

		//Get the list of files from input folder to process.
		List<Path> listOfInputFiles = FileUtil.getListOfFiles(config.getInputFolderPath());
		log.info("Received {} file(s)",	listOfInputFiles.size());

		//Process each file separately
		listOfInputFiles.parallelStream().forEach(path -> {
		    
            final List<StatementRecord> invalidAndDuplicateRecords = new ArrayList<>();
		    
			//Get the the time stamp of each file
			String fileIdentifier = FileUtil.extractFileIdentifier(path);

			//Transform input file records into java objects.
			List<StatementRecord> allRecords = inputFileUnmarshaller.getUnmarshaller(FileUtil.getFileExtension(path))
			                                                        .unmarshall(path);
			log.info("Number of records are {} in the input file {}",
					allRecords.size(), path.getFileName());

			//Validates input data and writes error records to error file
			List<StatementRecord> validatedRecords =
			fileValidationService.validateStatementRecords(allRecords, invalidAndDuplicateRecords);

			//Filter the duplicate records which are having same Transaction Reference number.
			List<StatementRecord> unquieTransactionReferenceRecords =
			duplicateRecordsService.filterDuplicateTransactionReferences(
			        validatedRecords, invalidAndDuplicateRecords);
			log.info("Unique number of transaction records are {}",
					unquieTransactionReferenceRecords.size());

			//Validate the statement balance
			List<StatementRecord> finalRecords =
			        statementBalanceService.validateBalance(
			                unquieTransactionReferenceRecords, invalidAndDuplicateRecords);
			log.info("Final number of transaction records are {}", finalRecords.size());

			//Write the results in to processed folder
			FileUtil.writeRecordsToFile(finalRecords, config.getProcessedFolderPath(),
					XYZBANK_FILE_PREFIX + fileIdentifier);
			
	        //Write invalidAndDuplicate records into Error file.
	        FileUtil.writeRecordsToFile(invalidAndDuplicateRecords, config.getErrorFolderPath(),
	                ERROR_PREFIX + fileIdentifier);

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

}
