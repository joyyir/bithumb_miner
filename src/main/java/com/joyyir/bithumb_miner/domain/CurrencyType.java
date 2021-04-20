package com.joyyir.bithumb_miner.domain;

import java.util.Arrays;

public enum CurrencyType {
    KRW("KRW"),
    BTC("BTC"),
    XRP("XRP");

    private String code;

    CurrencyType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CurrencyType findByCode(String code) {
        return Arrays.stream(CurrencyType.values())
                     .filter(x -> x.getCode().equals(code))
                     .findAny()
                     .orElseThrow(() -> new RuntimeException("CurrencyType not found for code: " + code));
    }
}
