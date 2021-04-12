package com.joyyir.bithumb_miner.infrastructure.util;

import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HTTPUtil {

    public static String paramsBuilder(MultiValueMap<String, String> map) {
        StringBuilder builder = new StringBuilder();
        for(String key : map.keySet()) {
            builder.append(key);
            builder.append("=");
            builder.append(map.get(key).get(0));
            builder.append("&");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    public static String encodeURIComponent(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8")
                             .replaceAll("\\+", "%20")
                             .replaceAll("\\%21", "!")
                             .replaceAll("\\%27", "'")
                             .replaceAll("\\%28", "(")
                             .replaceAll("\\%29", ")")
                             .replaceAll("\\%26", "&")
                             .replaceAll("\\%3D", "=")
                             .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
}
