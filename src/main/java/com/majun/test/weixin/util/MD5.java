package com.majun.test.weixin.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("prototype")
@Service(value = "md5")
public class MD5 {
	public String encodeAsNum(final String origin) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return byteArrayToString(md.digest(origin.getBytes()), 10);
	}

	public String encodeAsHex(final String origin) {
		if (origin == null)
			return null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			String str = byteArrayToString(md.digest(origin.getBytes("utf-8")), 16);
			return str;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String byteArrayToString(byte b[], int type) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			if (type == 16)
				resultSb.append(byteToHexString(b[i]));
			if (type == 10)
				resultSb.append(byteToNumString(b[i]));
		}

		return resultSb.toString();
	}

	private String byteToNumString(byte b) {
		int _b = b;
		if (_b < 0)
			_b += 256;
		return String.valueOf(_b);
	}

	private String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return (new StringBuilder(String.valueOf(HEX_DIGITS[d1]))).append(HEX_DIGITS[d2]).toString();
	}

	private final String HEX_DIGITS[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static void main(String[] args) throws Exception {	
		MD5 md= new MD5();
		System.out.println(md.encodeAsHex(""));
	}

}
