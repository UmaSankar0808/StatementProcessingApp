package com.xyzbank.customerstatementprocessor.model;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * StatementRecord.java - Bean class to map each record from the input file.
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatementRecord {

	private static final int FRACTION_SIZE = 2;
	private static final int NUMBER_OF_DIGITS = 6;

	@XmlAttribute
	@NotNull(message = "Transaction reference number should not be empty")
	@Min(value = 0, message = "reference shouldn't be a negative number")
	@CsvBindByName(column = "Reference")
	private Long reference;

	@NotNull(message = "AccountNumber should not be empty")
	@CsvBindByName(column = "AccountNumber")
	private String accountNumber;

	@NotNull(message = "Description should not be empty")
	@CsvBindByName(column = "Description")
	private String description;

	@NotNull(message = "startBalance should not be empty")
	@Digits(integer = NUMBER_OF_DIGITS, fraction = FRACTION_SIZE, message = "startBalance is invalid")
	@CsvBindByName(column = "Start Balance")
	private BigDecimal startBalance;

	@Digits(integer = NUMBER_OF_DIGITS, fraction = FRACTION_SIZE, message = "mutation is invalid")
	@CsvBindByName(column = "Mutation")
	private BigDecimal mutation;

	@NotNull(message = "EndBalance should not be empty")
	@Digits(integer = NUMBER_OF_DIGITS, fraction = FRACTION_SIZE, message = "endBalance is invalid")
	@CsvBindByName(column = "End Balance")
	private BigDecimal endBalance;

	@CsvBindByName(column = "Error")
	private String error;

}
