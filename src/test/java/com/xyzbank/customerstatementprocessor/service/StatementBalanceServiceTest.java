package com.xyzbank.customerstatementprocessor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;
import com.xyzbank.customerstatementprocessor.service.impl.StatementBalanceServiceImpl;
import com.xyzbank.customerstatementprocessor.util.StatementRecordBuilder;

class StatementBalanceServiceTest {
    
    private StatementBalanceService statementBalanceService = new StatementBalanceServiceImpl();

    @Test
    @DisplayName("Negative scenario: where end balance doesn't match with start balance + mutation")
    void shouldFilterOutRecordWithIncorrectEndBalance() {
        //Testing with single record where start balance + mutation != end balance
        StatementRecord record = StatementRecordBuilder.buildStatementRecord();
        record.setEndBalance(new BigDecimal(16.79));
        final List<StatementRecord> recordsWithInvalidEndBalance = new ArrayList<>();
        List<StatementRecord> recordsWithValidBalance = statementBalanceService
                .validateBalance(Arrays.asList(record), recordsWithInvalidEndBalance);
        assertTrue(recordsWithValidBalance.isEmpty());
        assertEquals("End balance doesn't match with start balance + mutation",
                recordsWithInvalidEndBalance.get(0).getError());
    }
    
    @Test
    @DisplayName("Positive scenario: where end balance matches with start balance + mutation")
    void shouldNotFilterOutAnyRecord() {
        //Testing with single record where start balance + mutation == end balance
        StatementRecord record = StatementRecordBuilder.buildStatementRecord();
        final List<StatementRecord> recordsWithInvalidEndBalance = new ArrayList<>();
        List<StatementRecord> recordsWithValidBalance = statementBalanceService
                .validateBalance(Arrays.asList(record), recordsWithInvalidEndBalance);
        assertTrue(recordsWithValidBalance.size() == 1);
    }
    
}
