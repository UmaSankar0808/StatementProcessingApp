package com.xyzbank.customerstatementprocessor.service;

import java.util.List;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;

public interface StatementBalanceService {

    List<StatementRecord> validateBalance(List<StatementRecord> unquieTransactionReferenceRecords,
           List<StatementRecord> invalidBalanceRecords);

}