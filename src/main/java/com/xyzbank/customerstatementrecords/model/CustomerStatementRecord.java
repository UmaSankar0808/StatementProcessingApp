package com.xyzbank.customerstatementrecords.model;

import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_FIVE;
import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_FOUR;
import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_ONE;
import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_THREE;
import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_TWO;
import static com.xyzbank.customerstatementrecords.util.Constants.COLUMN_ZERO;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerStatementRecord {

	@XmlAttribute
	@Min(value = 0, message = "reference shouldn't be a negative number")
	@CsvBindByPosition(position = COLUMN_ZERO)
	private Long reference;

	@CsvBindByPosition(position = COLUMN_ONE)
	private String accountNumber;

	@CsvBindByPosition(position = COLUMN_TWO)
	private String description;

	@Digits(integer = 6, fraction = 2, message = "startBalance is invalid")
	@CsvBindByPosition(position = COLUMN_THREE)
	private BigDecimal startBalance;

	@Digits(integer = 6, fraction = 2, message = "mutation is invalid")
	@CsvBindByPosition(position = COLUMN_FOUR)
	private BigDecimal mutation;

	@Digits(integer = 6, fraction = 2, message = "endBalance is invalid")
	@CsvBindByPosition(position = COLUMN_FIVE)
	private BigDecimal endBalance;

}
