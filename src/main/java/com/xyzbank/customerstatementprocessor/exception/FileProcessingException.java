package com.xyzbank.customerstatementprocessor.exception;

@SuppressWarnings("serial")
public class FileProcessingException extends RuntimeException {

	public FileProcessingException(final String message) {
		super(message);
	}

}
