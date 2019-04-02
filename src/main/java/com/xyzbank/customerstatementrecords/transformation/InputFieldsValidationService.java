package com.xyzbank.customerstatementrecords.transformation;

import static com.xyzbank.customerstatementrecords.util.Constants.ERROR_PREFIX;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

import com.xyzbank.customerstatementrecords.configuration.ApplicationConfig;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecord;
import com.xyzbank.customerstatementrecords.model.CustomerStatementRecordWithError;
import com.xyzbank.customerstatementrecords.util.FileUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InputFieldsValidationService {

	private ApplicationConfig config;

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	public void validateCustomerStatementRecords(final List<CustomerStatementRecord> records,
			final String fileIdentifier) {
		List<CustomerStatementRecord> validRecords = new ArrayList<>();
		List<CustomerStatementRecord> invalidRecords = new ArrayList<>();
        records.forEach(record -> {
            Set<ConstraintViolation<CustomerStatementRecord>> constraintViolations =
                    VALIDATOR.validate(record);
            if (constraintViolations.isEmpty()) {
            	validRecords.add(record);
            } else {
                String errorMessage = constraintViolations.stream().map(ConstraintViolation::getMessage)
                                                          .collect(joining(", "));
                log.error(errorMessage);
                CustomerStatementRecordWithError recordwitherror =
                		new CustomerStatementRecordWithError(record);
                recordwitherror.setError(errorMessage);
                invalidRecords.add(recordwitherror);
            }
        });
        FileUtil.writeRecordsToFile(invalidRecords, config.getErrorFolderPath(),
				ERROR_PREFIX + fileIdentifier, true);
	}

}
