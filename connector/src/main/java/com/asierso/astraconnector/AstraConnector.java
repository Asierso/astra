package com.asierso.astraconnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import com.asierso.astracommons.SecureCipher;
import com.asierso.astraconnector.actions.Action;
import com.asierso.astracommons.requests.ClientActions;
import com.asierso.astracommons.requests.ClientRequest;
import com.asierso.astracommons.requests.ClientResponse;
import com.google.gson.Gson;

public class AstraConnector {
	private Socket client;
	private PrintStream out;
	private BufferedReader in;
	private String token;
	private SecureCipher crypt;
	
	public AstraConnector(String ip, int port, String token) throws UnknownHostException, IOException, Exception {
		client = new Socket(ip,port);
		out = new PrintStream(client.getOutputStream());
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		if(token!=null && !token.isBlank()) {
			this.token = token;
		}
		
		crypt = new SecureCipher();
		out.println(crypt.getPubkey());
		crypt.setTargetPubkey(in.readLine());
	}
	
	public ClientResponse sendRequest(ClientRequest req) throws Exception{
		req.setToken(token);
		out.println(crypt.encrypt(new Gson().toJson(req)));
		return new Gson().fromJson(crypt.decrypt(in.readLine()),ClientResponse.class);
	}
	
	public Object fetch(Action action) throws Exception {
		return action.getClass().getConstructor(null).newInstance(null).run(this);
	}
	
	public void close() throws IOException {
		client.close();
	}
}
