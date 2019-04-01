package com.xyzbank.customerstatementrecords.exception;

@SuppressWarnings("serial")
public class FileProcessingException extends RuntimeException {

	public FileProcessingException(final String message) {
		super(message);
	}

}
