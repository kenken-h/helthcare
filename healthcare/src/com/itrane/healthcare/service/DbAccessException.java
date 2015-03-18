package com.itrane.healthcare.service;

public class DbAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DbAccessException(String msg) {
		super(msg);
	}

	public DbAccessException(String msg, Throwable t) {
		super(msg, t);
	}
}
