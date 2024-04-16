package com.asierso.astracommons.exceptions;

public class AutenticationException extends Exception{
	public AutenticationException() {
		super("Invalid token");
	}
}
