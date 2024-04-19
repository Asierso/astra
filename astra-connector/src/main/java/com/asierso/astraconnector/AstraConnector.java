package com.asierso.astraconnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.asierso.astracommons.SecureCipher;
import com.asierso.astracommons.exceptions.RequestException;
import com.asierso.astraconnector.actions.Action;
import com.asierso.astracommons.requests.ClientActions;
import com.asierso.astracommons.requests.ClientRequest;
import com.asierso.astracommons.requests.ClientResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class AstraConnector {
	private Socket client;
	private PrintStream out;
	private BufferedReader in;
	private String token;
	private SecureCipher crypt;
	
	public AstraConnector(String ip, int port) throws UnknownHostException, IOException, Exception {
		initConnector(ip, port, null); //Unprotected astra server mode
	}

	public AstraConnector(String ip, int port, String token) throws UnknownHostException, IOException, Exception {
		initConnector(ip, port, token);
	}
	
	private void initConnector(String ip, int port, String token) throws UnknownHostException, IOException, Exception {
		client = new Socket(ip, port);
		out = new PrintStream(client.getOutputStream());
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));

		if (token != null && !token.isBlank()) {
			this.token = token;
		}

		try {
			keyHandshake();
		} catch (Exception e) {
			client.close();
			throw new Exception("Error at key handshake");
		}
	}

	public ClientResponse sendRequest(ClientRequest req) throws RequestException, Exception{
		req.setToken(token);
		try {
			out.println(crypt.symEncrypt(new Gson().toJson(req)));
			return new Gson().fromJson(crypt.symDecrypt(in.readLine()), ClientResponse.class);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidParameterSpecException
				| IllegalBlockSizeException | BadPaddingException e) {
			throw new Exception("Error at request encrypt");
		} catch (IOException | JsonSyntaxException e) {
			throw new RequestException(505, "Bad request or server error");
		}
		
	}

	public Object fetch(Action action) throws Exception {
		return action.run(this);
	}

	public void close() throws IOException {
		client.close();
	}

	private void keyHandshake() throws Exception {
		crypt = new SecureCipher();
		out.println(crypt.getPubkey());
		crypt.setTargetPubkey(in.readLine());
		crypt.setDecryptSimkey(crypt.asymDecrypt(in.readLine()));
	}
}
