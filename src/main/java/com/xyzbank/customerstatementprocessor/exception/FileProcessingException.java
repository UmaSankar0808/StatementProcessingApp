package com.xyzbank.customerstatementprocessor.exception;

/**
 * FileProcessingException.java - Custom exception class to handle File processing exceptions
 *
 */
@SuppressWarnings("serial")
public class FileProcessingException extends RuntimeException {

	public FileProcessingException(final String message) {
		super(message);
	}

}
