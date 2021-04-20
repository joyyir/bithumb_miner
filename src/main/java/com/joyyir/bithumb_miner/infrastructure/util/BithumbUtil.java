package com.joyyir.bithumb_miner.infrastructure.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class BithumbUtil {
    public static MultiValueMap<String, String> privateApiHeader(String apiKey, String secretKey, String endpoint, MultiValueMap<String, String> params) {
        final String nonce = String.valueOf(System.currentTimeMillis());
        final String strParams = HTTPUtil.paramsBuilder(params);
        final String encodedParams = HTTPUtil.encodeURIComponent(strParams);
        final String str = endpoint + ";" + encodedParams + ";" + nonce;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Api-Key", apiKey);
        headers.add("Api-Sign", Encryptor.getHmacSha512(secretKey, str, Encryptor.EncodeType.BASE64));
        headers.add("Api-Nonce", nonce);
        headers.add("api-client-type", "2");
        return headers;
    }
}
