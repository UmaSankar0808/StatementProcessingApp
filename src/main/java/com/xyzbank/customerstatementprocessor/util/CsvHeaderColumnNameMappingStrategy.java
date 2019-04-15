package com.xyzbank.customerstatementprocessor.util;

import java.util.Arrays;
import java.util.List;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.xyzbank.customerstatementprocessor.model.StatementRecord;

/**
 * CsvHeaderColumnNameMappingStrategy.java - Extends OpenCSV class
 * HeaderColumnNameMappingStrategy to override the default mapping strategy
 */
public class CsvHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    private static CsvHeaderColumnNameMappingStrategy<StatementRecord> strategy;

    private static final List<String> CSV_HEADERS = Arrays.asList("REFERENCE", "ACCOUNTNUMBER", "DESCRIPTION",
            "START BALANCE", "MUTATION", "END BALANCE", "ERROR");

	/**
	 * Returns the CsvHeaderColumnNameMappingStrategy singleton object
	 * @return CsvHeaderColumnNameMappingStrategy object
	 */
	public static final CsvHeaderColumnNameMappingStrategy<StatementRecord> getInstance() {
	    if (strategy == null) {
	        strategy = new CsvHeaderColumnNameMappingStrategy<>();
	        strategy.setType(StatementRecord.class);
            strategy.setColumnOrderOnWrite((o1, o2) -> CSV_HEADERS.indexOf(o1) - CSV_HEADERS.indexOf(o2));
	    }
	    return strategy;
	}

    @Override
    public boolean isAnnotationDriven() {
        return true;
    }
}