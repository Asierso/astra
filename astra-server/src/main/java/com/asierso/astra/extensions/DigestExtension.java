package com.asierso.astra.extensions;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.asierso.astra.FileManager;

public class DigestExtension {
	public static String toMD5(String key) {
		if(key == null || key.isBlank())
			return null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(key.getBytes());
			byte[] bytes = digest.digest();
			return new String(bytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean verifyToken(String key) {
		if (new File("models/.token").exists()) {
			if (key == null || key.isBlank()) // Key is null or blank
				return false;
			try { // Read and compare keys
				FileManager token = new FileManager(".token");
				return toMD5(key).trim()
						.equals(token.readByLines().trim());
			} catch (FileNotFoundException e) { //No key file
				return true;
			}
		}

		// Server unprotected
		return true;
	}
}
