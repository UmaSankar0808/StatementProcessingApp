package com.xyzbank.customerstatementprocessor.util;
import java.util.Arrays;
import java.util.List;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;

public class CsvHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {
    
    private static CsvHeaderColumnNameMappingStrategy<StatementRecord> strategy;

    private static final List<String> csvHeaders =Arrays.asList("REFERENCE", "ACCOUNTNUMBER", "DESCRIPTION",
            "START BALANCE", "MUTATION", "END BALANCE", "ERROR");
	
	public static final CsvHeaderColumnNameMappingStrategy<StatementRecord> getInstance() {
	    if (strategy == null) {
	        strategy = new CsvHeaderColumnNameMappingStrategy<>();
	        strategy.setType(StatementRecord.class);
            strategy.setColumnOrderOnWrite((o1, o2) -> {
                return csvHeaders.indexOf(o1) - csvHeaders.indexOf(o2);   
            });
	    }
	    return strategy;
	}

    @Override
    public boolean isAnnotationDriven() {
        return true;
    }
}