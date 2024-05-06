package com.asierso.astracommons;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecureCipher {
	private KeyPair keyPair;
	private PublicKey pubkey;
	private SecretKey simkey;
	public SecureCipher() throws NoSuchAlgorithmException{
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(2048);
		keyPair = keygen.generateKeyPair();
	}
	
	public String asymEncrypt(String text) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
	}
	
	public String asymDecrypt(String ciphertext) throws Exception {
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
	
	public String generateSimkey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        this.simkey = secretKey;
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
	
	public void setDecryptSimkey(String simkey) {
		byte[] decodedKey = Base64.getDecoder().decode(simkey);
        this.simkey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}
	
	public String symEncrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, simkey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
	}
	
	public String symDecrypt(String ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, simkey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)),StandardCharsets.UTF_8);
	}
}
