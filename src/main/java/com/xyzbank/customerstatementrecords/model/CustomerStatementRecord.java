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
import javax.validation.constraints.NotNull;
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

	private static final int FRACTION_SIZE = 2;
	private static final int NUMBER_OF_DIGITS = 6;

	@XmlAttribute
	@NotNull(message = "Transaction reference number should not be empty")
	@Min(value = 0, message = "reference shouldn't be a negative number")
	@CsvBindByPosition(position = COLUMN_ZERO)
	private Long reference;

	@NotNull(message = "AccountNumber should not be empty")
	@CsvBindByPosition(position = COLUMN_ONE)
	private String accountNumber;

	@NotNull(message = "Description should not be empty")
	@CsvBindByPosition(position = COLUMN_TWO)
	private String description;

	@NotNull(message = "startBalance should not be empty")
	@Digits(integer = NUMBER_OF_DIGITS, fraction = FRACTION_SIZE, message = "startBalance is invalid")
	@CsvBindByPosition(position = COLUMN_THREE)
	private BigDecimal startBalance;

	@Digits(integer = NUMBER_OF_DIGITS, fraction = FRACTION_SIZE, message = "mutation is invalid")
	@CsvBindByPosition(position = COLUMN_FOUR)
	private BigDecimal mutation;

	@NotNull(message = "EndBalance should not be empty")
	@Digits(integer = NUMBER_OF_DIGITS, fraction = FRACTION_SIZE, message = "endBalance is invalid")
	@CsvBindByPosition(position = COLUMN_FIVE)
	private BigDecimal endBalance;

}
