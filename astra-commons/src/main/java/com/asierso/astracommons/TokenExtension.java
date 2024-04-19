package com.asierso.astracommons;

import java.util.Random;

public class TokenExtension {
	public static String generateToken(int length) {
		char[] keydict = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
		StringBuilder res = new StringBuilder();
		
		Random rdm = new Random();
		
		for(int i = 0;i<length;i++) {
			res.append(keydict[rdm.nextInt(0,keydict.length)]);
		}
		return "atk_"+res.toString();
	}
}
