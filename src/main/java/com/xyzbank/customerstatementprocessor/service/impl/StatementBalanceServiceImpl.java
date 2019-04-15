package com.xyzbank.customerstatementprocessor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.StatementBalanceService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * StatementBalanceServiceImpl.java - This service class is responsible for final balance verification.
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class StatementBalanceServiceImpl implements StatementBalanceService {

	/**
	 * Validate the statement balance.
	 *
	 * @param list of StatementRecord objects
	 * @param list of invalid balance StatementRecord objects
	 * @return list of valid StatementRecord objects
	 */
    public List<StatementRecord> validateBalance(
            final List<StatementRecord> unquieTransactionReferenceRecords,
            final List<StatementRecord> invalidBalanceRecords) {
        List<StatementRecord> validRecords = new ArrayList<>();
        unquieTransactionReferenceRecords.forEach(record -> {
            if (record.getStartBalance().add(record.getMutation()).compareTo(record.getEndBalance()) == 0) {
                validRecords.add(record);
            } else {
            	log.error("End balance doesn't match with start balance + mutation");
                record.setError("End balance doesn't match with start balance + mutation");
                invalidBalanceRecords.add(record);
            }
        });
        return validRecords;
    }
}
