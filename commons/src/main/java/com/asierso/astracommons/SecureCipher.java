package com.asierso.astracommons;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class SecureCipher {
	private KeyPair keyPair;
	private PublicKey pubkey;
	public SecureCipher() throws NoSuchAlgorithmException{
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(2048);
		keyPair = keygen.generateKeyPair();
	}
	
	public String encrypt(String text) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
	}
	
	public String decrypt(String ciphertext) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE,keyPair.getPrivate());
		return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)),StandardCharsets.UTF_8);
	}
	
	public void setTargetPubkey(String pubkey) throws Exception {
		X509EncodedKeySpec keyspc = new X509EncodedKeySpec(Base64.getDecoder().decode(pubkey));
		KeyFactory keyFact = KeyFactory.getInstance("RSA");
		this.pubkey = keyFact.generatePublic(keyspc);
	}
	
	public String getPubkey() {
		return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
	}
}
