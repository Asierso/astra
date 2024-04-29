package com.asierso.astraconnector;

public class AstraConnectionBuilder extends AstraConnection {
	
	public AstraConnectionBuilder() {
		
	}
	
	public AstraConnectionBuilder withIp(String ip) {
		this.ip = ip;
		return this;
	}
	
	public AstraConnectionBuilder withPort(int port) {
		this.port = port;
		return this;
	}
	
	public AstraConnectionBuilder withToken(String token) {
		this.token = token;
		return this;
	}
	
	public AstraConnection build() {
		return this;
	}
}
