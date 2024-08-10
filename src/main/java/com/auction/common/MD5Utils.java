package com.auction.common;


import org.apache.commons.codec.digest.DigestUtils;


public class MD5Utils {
	public static String md5(String src){
		return DigestUtils.md5Hex(src);
	}

	private static final String salt = "1a2b3c4d";
	//用户输入信息加密传递到后台
	public static String inputPassToFormPass(String inputPass){
		String str =""+ salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);

		return md5(str);
	}
	//传递到后台的加密信息二次加密存到数据库
	public static String formPassToDBPass(String formPass,String salt){
		String str = ""+salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}

	public static String inputPassToDbPass(String input,String saltDB){
		String formPass = inputPassToFormPass(input);
		String dbPass = formPassToDBPass(formPass,saltDB);
		return dbPass;
	}
}
