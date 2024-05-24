package com.asierso.astraconnector;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import com.asierso.astracommons.SecureCipher;
import com.asierso.astracommons.exceptions.RequestException;
import com.asierso.astraconnector.actions.Action;
import com.asierso.astraconnector.connection.AstraConnection;
import com.asierso.astracommons.requests.ClientRequest;
import com.asierso.astracommons.requests.ClientResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class AstraConnector implements Closeable{
	//Socket and streams
	private Socket client;
	private PrintStream out;
	private BufferedReader in;
	
	//Connection and handshake
	private AstraConnection currentConnection;
	private SecureCipher crypt;
	
	/**
	 * Initialize connection with Astra Server
	 * @param builtConnection Server connection parameters 
	 * @throws UnknownHostException Server host not found or the port is incorrect
	 * @throws IOException Exception in data streaming
	 * @throws Exception Other exceptions
	 */
	public AstraConnector(AstraConnection builtConnection) throws UnknownHostException, IOException, Exception {
		//Create connection by builder
		client = new Socket(builtConnection.getIp(), builtConnection.getPort());
		out = new PrintStream(client.getOutputStream());
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		this.currentConnection = builtConnection;

		//Try handshake (prepare ciphers)
		try {
			keyHandshake();
		} catch (Exception e) {
			client.close();
			throw new Exception("Error at key handshake");
		}
	}

	public ClientResponse sendRequest(ClientRequest req) throws RequestException, Exception{
		//Set token (if is established)
		if (currentConnection.getToken() != null) 
			req.setToken(currentConnection.getToken());
		
		//Make request
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

	/**
	 * Run an action inside Astra Server. Actions must implement "Action" interface to be runnable
	 * @param action New action to run in Astra
	 * @return Server response
	 * @throws Exception
	 */
	public Object fetch(Action action) throws Exception {
		return action.run(this);
	}
	
	private void keyHandshake() throws Exception {
		crypt = new SecureCipher();
		out.println(crypt.getPubkey());
		crypt.setTargetPubkey(in.readLine());
		crypt.setDecryptSimkey(crypt.asymDecrypt(in.readLine()));
	}

	/**
	 * Close client socket with Astra. Compatible with try with resources
	 */
	@Override
	public void close() throws IOException {
		client.close();
	}
}
