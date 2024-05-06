package com.asierso.astraconnector.connection;

public class AstraConnection {
	protected String ip;
	protected int port;
	protected String token;
	
	public AstraConnection() {
		
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getToken() {
		return token;
	}
}
