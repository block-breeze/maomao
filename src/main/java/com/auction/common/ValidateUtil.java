package com.auction.common;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * @Author zaria
 * @Date 2021/5/1
 */
public class ValidateUtil{

    //手机号验证类的方法
    public static String validateMobile(String mobile) {
        if (StringUtils.isNotBlank(mobile)) {
            if (mobile.length() > 3) {
                String first = mobile.substring(0, 3);
                String last = mobile.substring(mobile.length() - 4);
                StringBuilder newmobile = new StringBuilder();
                if (mobile.length() >= 7) {
                    for (int i = 0; i < mobile.length() - 7; i++) {
                        newmobile.append("*");
                    }
                    newmobile = new StringBuilder(first + newmobile + last);
                    return newmobile.toString();
                } else {
                    return mobile;
                }
            } else {
                return mobile;
            }
        }
        return "";
    }

    //身份证验证方法
    public static String validateIdCard(String idCard) {
        if (StringUtils.isNotBlank(idCard)) {
            String first = "";
            String last = "";
            StringBuilder newIdCard = null;
            int floatPoint = 0;
            if (idCard.length() >= 12) {
                floatPoint = 4;
            } else if (idCard.length() >= 6 && idCard.length() < 12) {
                floatPoint = 2;
            } else if (idCard.length() > 2 && idCard.length() < 6) {
                floatPoint = 1;
            }
            if (floatPoint != 0) {
                first = idCard.substring(0, floatPoint);
                last = idCard.substring(idCard.length() - floatPoint);
                newIdCard = new StringBuilder();
                for (int i = 0; i < idCard.length() - floatPoint * 2; i++) {
                    newIdCard.append("*");
                }
                newIdCard = new StringBuilder(first + newIdCard + last);
                return newIdCard.toString();
            } else {
                return idCard;
            }
        }
        return "";
    }
}
