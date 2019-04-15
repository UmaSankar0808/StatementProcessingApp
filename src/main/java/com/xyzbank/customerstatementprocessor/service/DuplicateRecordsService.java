package com.xyzbank.customerstatementprocessor.service;

import java.util.List;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;

public interface DuplicateRecordsService {

    List<StatementRecord> filterDuplicateTransactionReferences(
           List<StatementRecord> records, List<StatementRecord> duplicateRecords);
}