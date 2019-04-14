package com.xyzbank.customerstatementprocessor.util;

import java.math.BigDecimal;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatementRecordBuilder {

    public static StatementRecord buildStatementRecord() {
        return StatementRecord.builder()
                              .accountNumber("123456")
                              .description("Free Text")
                              .endBalance(new BigDecimal("-26.78"))
                              .startBalance(new BigDecimal("-16.78"))
                              .mutation(new BigDecimal("-10.00"))
                              .reference(15455634L)
                              .build();
    }
    
}
