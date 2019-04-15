package com.xyzbank.customerstatementprocessor.service.impl;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.InputFileValidationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * InputFileValidationServiceImpl.java - This class is responsible
 * for doing validations on the Bean class StatementRecord.java
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class InputFileValidationServiceImpl implements InputFileValidationService {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * Validates input data and returns list of validated StatementRecord objects.
	 *
	 * @param list of StatementRecord objects
	 * @param list of invalid StatementRecord objects
	 * @return list of valid StatementRecord objects
	 */
	public List<StatementRecord> validateStatementRecords(final List<StatementRecord> records,
	        final List<StatementRecord> invalidRecords) {
		List<StatementRecord> validRecords = new ArrayList<>();
        records.forEach(record -> {
            Set<ConstraintViolation<StatementRecord>> constraintViolations = VALIDATOR.validate(record);
            if (constraintViolations.isEmpty()) {
            	validRecords.add(record);
            } else {
                String errorMessage = constraintViolations.stream().map(ConstraintViolation::getMessage)
                                                          .collect(joining(", "));
                log.error(errorMessage);
                record.setError(errorMessage);
                invalidRecords.add(record);
            }
        });
        return validRecords;
	}

}
