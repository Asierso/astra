package com.asierso.astra.exceptions;

public class AutenticationException extends Exception{
	public AutenticationException() {
		super("Invalid token");
	}
}
