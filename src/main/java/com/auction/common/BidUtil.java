package com.auction.common;


import org.springframework.context.ApplicationContext;
import java.util.Random;

public class BidUtil {
	
	public static ApplicationContext ac = null;

	private static String ALPHABET= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static String DIGIT= "0123456789";
	
	public static String genRandomNum(){
		Random random = new Random();   
        StringBuffer sb = new StringBuffer();
        int number = 0;
        for (int i = 0; i < 5; i++) {
        	if (i == 0) {
        		number = random.nextInt(ALPHABET.length());   
                sb.append(ALPHABET.charAt(number)); 
        	} else {
        		number = random.nextInt(DIGIT.length());   
                sb.append(DIGIT.charAt(number)); 
        	}
        }
        return sb.toString();
	}

    public static String genCapcha() {
        String[] target = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int rd = (int) (Math.random() * 9000) % target.length;
            sb.append(target[rd]);
        }
        return sb.toString();
    }

    public static String genRandomOrderNum() {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < 64; i++) {
            boolean b = random.nextBoolean();
            if (b) { // 字符串
                // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
                str += (char) (65 + random.nextInt(26));// 取得大写字母
            } else { // 数字
                str += String.valueOf(random.nextInt(10));
            }
        }
        return str;


    }

}
