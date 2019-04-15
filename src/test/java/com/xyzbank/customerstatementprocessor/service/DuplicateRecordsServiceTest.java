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
    void shouldFilterOutDuplicatesWithSameTransactionReference() {
        //record1 & record2 has same transaction reference
        StatementRecord record1 = StatementRecordBuilder.buildStatementRecord();
        StatementRecord record2 = StatementRecordBuilder.buildStatementRecord();
        StatementRecord record3 = StatementRecordBuilder.buildStatementRecord();
        //Setting unique transaction reference to record3
        record3.setReference(543543L);
        final List<StatementRecord> duplicateRecords = new ArrayList<>();
        List<StatementRecord> validUniqueRecords = duplicateRecordsService.filterDuplicateTransactionReferences(
                        Arrays.asList(record1, record2, record3), duplicateRecords);
        assertEquals(Arrays.asList(record3), validUniqueRecords);
        assertEquals(Arrays.asList(record1, record2), duplicateRecords);
    }

}
