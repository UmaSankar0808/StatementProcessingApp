package com.xyzbank.customerstatementrecords.util;

public final class Constants {

	private Constants() {
		throw new UnsupportedOperationException(
		        "You are not allowed to instantiate " + this.getClass().getName() + " class");
	}

	public static final String EXTENSION_CSV = ".csv";
	public static final String EXTENSION_XML = ".xml";
	public static final int FILE_EXTENSION_LENGTH = 4;
	public static final String RABOBANK_FILE_PREFIX = "records_";
	public static final String ERROR_PREFIX = "records_";
	public static final String COLUMN_HEADERS =
			"Reference,AccountNumber,Description,Start Balance,Mutation,End Balance\n";
	public static final String COLUMN_HEADERS_WITH_ERROR =
			"Reference,AccountNumber,Description,Start Balance,Mutation,End Balance,Error\n";
	public static final int COLUMN_ZERO = 0;
	public static final int COLUMN_ONE = 1;
	public static final int COLUMN_TWO = 2;
	public static final int COLUMN_THREE = 3;
	public static final int COLUMN_FOUR = 4;
	public static final int COLUMN_FIVE = 5;
	public static final int COLUMN_SIX = 6;
}
