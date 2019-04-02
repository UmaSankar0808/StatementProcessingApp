package com.xyzbank.customerstatementrecords.model;

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
	@CsvBindByPosition(position = 0)
	private Long reference;

	@CsvBindByPosition(position = 1)
	private String accountNumber;

	@CsvBindByPosition(position = 2)
	private String description;

	@Digits(integer = 6, fraction = 2, message = "startBalance is invalid")
	@CsvBindByPosition(position = 3)
	private BigDecimal startBalance;

	@Digits(integer = 6, fraction = 2, message = "mutation is invalid")
	@CsvBindByPosition(position = 4)
	private BigDecimal mutation;

	@Digits(integer = 6, fraction = 2, message = "endBalance is invalid")
	@CsvBindByPosition(position = 5)
	private BigDecimal endBalance;

}
