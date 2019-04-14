package com.xyzbank.customerstatementprocessor.service;

import java.util.List;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;

public interface InputFileValidationService {

    public List<StatementRecord> validateStatementRecords(
           List<StatementRecord> records, List<StatementRecord> invalidRecords);

}