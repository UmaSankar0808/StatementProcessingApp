package com.xyzbank.customerstatementprocessor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.StatementBalanceService;
import com.xyzbank.customerstatementprocessor.service.impl.StatementBalanceServiceImpl;
import com.xyzbank.customerstatementprocessor.util.StatementRecordBuilder;

class StatementBalanceServiceTest {
    
    private StatementBalanceService statementBalanceService = new StatementBalanceServiceImpl();

    @Test
    void shouldFilterOutRecordWithIncorrectEndBalance() throws IOException {
        StatementRecord record = StatementRecordBuilder.buildStatementRecord();
        record.setEndBalance(new BigDecimal(16.79));
        final List<StatementRecord> invalidRecords = new ArrayList<>();
        List<StatementRecord> actualResult = statementBalanceService
                .validateBalance(Arrays.asList(record), invalidRecords);
        assertTrue(actualResult.isEmpty());
        assertEquals("End balance doesn't match with start balance + mutation",
                invalidRecords.get(0).getError());
    }
    
}
