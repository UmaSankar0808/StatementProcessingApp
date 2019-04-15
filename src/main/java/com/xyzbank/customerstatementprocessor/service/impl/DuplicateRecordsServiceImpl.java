package com.xyzbank.customerstatementprocessor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.DuplicateRecordsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DuplicateRecordsServiceImpl.java - This java class is used to filter out  duplicate records
 * based on transaction reference
 * 
 */
@Slf4j
@Service
@AllArgsConstructor
public class DuplicateRecordsServiceImpl implements DuplicateRecordsService {

	/**
	 * Filter the duplicate records which are having same transaction reference number.
	 *
	 * @param list of StatementRecord objects
	 * @param list of duplicate StatementRecord objects
	 * @return list of unique StatementRecord objects
	 */
    public List<StatementRecord> filterDuplicateTransactionReferences(
            final List<StatementRecord> records, final List<StatementRecord> duplicateRecords) {
        Map<Long, List<StatementRecord>> groupRecordsByTransactionReference = new HashMap<>();
        List<StatementRecord> uniqueTransactionRecords = new ArrayList<>();
        records.forEach(record -> {
            List<StatementRecord> statementRecords =
                    groupRecordsByTransactionReference.get(record.getReference());
            if (statementRecords == null) {
                statementRecords = new ArrayList<>();
                groupRecordsByTransactionReference.put(record.getReference(), statementRecords);
            }
            statementRecords.add(record);
        });
        groupRecordsByTransactionReference.forEach((key, value) -> {
            List<StatementRecord> statementRecords = value;
            if (statementRecords.size() == 1) {
                uniqueTransactionRecords.addAll(statementRecords);
            } else {
            	log.error("Duplicate transaction reference exists");
                statementRecords.forEach(
                        record -> record.setError("Duplicate transaction reference exists"));
                duplicateRecords.addAll(statementRecords);
            }
        });
        return uniqueTransactionRecords;
    }

}
