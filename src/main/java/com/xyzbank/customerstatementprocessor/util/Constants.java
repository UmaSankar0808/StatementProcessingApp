package com.xyzbank.customerstatementprocessor.util;

import java.util.Arrays;
import java.util.List;

/**
 * Constants.java - Holds all the constants required across the application.
 *
 */
public final class Constants {

	private Constants() {
		throw new UnsupportedOperationException(
		        "You are not allowed to instantiate " + this.getClass().getName() + " class");
	}

	public static final String EXTENSION_CSV = ".csv";
	public static final String EXTENSION_XML = ".xml";
	public static final int FILE_EXTENSION_LENGTH = 4;
	public static final String XYZBANK_FILE_PREFIX = "records_";
	public static final String ERROR_PREFIX = "records_";
	public static final String COLUMN_HEADERS =
			"Reference,AccountNumber,Description,Start Balance,Mutation,End Balance\n";
	public static final String COLUMN_HEADERS_WITH_ERROR =
			"Reference,AccountNumber,Description,Start Balance,Mutation,End Balance,Error\n";
	protected static final List<String> VALID_FILE_EXTENSIONS = Arrays.asList(".csv", ".xml");
}
