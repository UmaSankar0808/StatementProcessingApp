package com.xyzbank.customerstatementprocessor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;

/**
 * StatementRecords.java - This class maps the XML records 
 * to StatementRecord Bean by taking records as the XML root element
 *
 */
@Getter
@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatementRecords {

	private List<StatementRecord> record;

}
