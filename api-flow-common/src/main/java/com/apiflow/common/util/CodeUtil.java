package com.apiflow.common.util;

import cn.hutool.core.date.DateUtil;

import java.security.SecureRandom;
import java.util.Date;

public class CodeUtil {
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(secureRandom.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    public static String generateCode(int length) {
        return DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + generate(length);
    }

}
