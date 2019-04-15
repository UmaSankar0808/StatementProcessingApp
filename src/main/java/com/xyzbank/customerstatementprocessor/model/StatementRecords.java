package com.xyzbank.customerstatementprocessor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;

/**
 * StatementRecords.java - Model class to map xml file to java bean
 *
 */
@Getter
@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatementRecords {

	private List<StatementRecord> record;

}
