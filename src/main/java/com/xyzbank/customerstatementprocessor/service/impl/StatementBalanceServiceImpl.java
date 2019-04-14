package com.xyzbank.customerstatementprocessor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.StatementBalanceService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class StatementBalanceServiceImpl implements StatementBalanceService {
    
    public List<StatementRecord> validateBalance(
            final List<StatementRecord> unquieTransactionReferenceRecords,
            final List<StatementRecord> invalidBalanceRecords) {
        List<StatementRecord> validRecords = new ArrayList<>();
        unquieTransactionReferenceRecords.forEach(record -> {
            if (record.getStartBalance().add(record.getMutation()).compareTo(record.getEndBalance()) == 0) {
                validRecords.add(record);
            } else {
                record.setError("End balance doesn't match with start balance + mutation");
                invalidBalanceRecords.add(record);
            }
        });
        return validRecords;
    }
}
