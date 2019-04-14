package com.xyzbank.customerstatementprocessor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.DuplicateRecordsService;
import com.xyzbank.customerstatementprocessor.service.impl.DuplicateRecordsServiceImpl;
import com.xyzbank.customerstatementprocessor.util.StatementRecordBuilder;

class DuplicateRecordsServiceTest {

    private DuplicateRecordsService duplicateRecordsService = new DuplicateRecordsServiceImpl();
    
    @Test
    @DisplayName("Should filter out records with same transaction reference")
    void shouldFilterOutDuplicatesWithSameTransactionReferences() {
        StatementRecord record1 = StatementRecordBuilder.buildStatementRecord();
        StatementRecord record2 = StatementRecordBuilder.buildStatementRecord();
        StatementRecord record3 = StatementRecordBuilder.buildStatementRecord();
        record3.setReference(543543L);
        final List<StatementRecord> invalidRecords = new ArrayList<>();
        List<StatementRecord> uniqueRecords = duplicateRecordsService.filterDuplicateTransactionReferences(
                        Arrays.asList(record1, record2, record3), invalidRecords);
        assertEquals(Arrays.asList(record3), uniqueRecords);
        assertEquals(Arrays.asList(record1, record2), invalidRecords);
    }

}
