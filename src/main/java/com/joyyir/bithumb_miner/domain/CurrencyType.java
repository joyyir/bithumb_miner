package com.joyyir.bithumb_miner.domain;

public enum CurrencyType {
    KRW("KRW"),
    BTC("BTC");

    private String code;

    CurrencyType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
