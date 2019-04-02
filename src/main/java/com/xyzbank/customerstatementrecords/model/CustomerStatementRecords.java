package com.xyzbank.customerstatementrecords.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;

@Getter
@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerStatementRecords {

	private List<CustomerStatementRecord> record;

}
