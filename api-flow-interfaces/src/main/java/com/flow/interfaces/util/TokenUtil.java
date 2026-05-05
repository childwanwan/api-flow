package com.flow.interfaces.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;

public class TokenUtil {

    public static final String AUTH_TOKEN = "authToken";

    public static String getTokenFromCookie(HttpServletRequest request) {
        if (ObjectUtils.isEmpty(request)){
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
