package com.api.users.utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtil {

    public static String normalizeText(String text) {
        return text.toLowerCase();
    }

    public static Map<String, Object> responseMap(String message, int code) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);
        responseMap.put("code", code);
        return responseMap;
    }

}
