package com.example.exception;

public class BookingConflictException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookingConflictException(String message) {
        super(message);
    }
}