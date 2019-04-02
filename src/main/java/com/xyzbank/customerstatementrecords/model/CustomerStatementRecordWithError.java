package com.xyzbank.customerstatementrecords.model;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class CustomerStatementRecordWithError extends CustomerStatementRecord {

	public CustomerStatementRecordWithError(final CustomerStatementRecord customerStatementRecord) {
		try {
			BeanUtils.copyProperties(this, customerStatementRecord);
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}

	@CsvBindByPosition(position = 6)
	private String error;
}
