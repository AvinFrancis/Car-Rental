package com.example.exception;

public class PaymentFailedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaymentFailedException(String message) {
        super(message);
    }
}