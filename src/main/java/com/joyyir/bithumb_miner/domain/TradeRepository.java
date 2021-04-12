package com.joyyir.bithumb_miner.domain;

import java.math.BigDecimal;

public interface TradeRepository {
    String marketBuy(BigDecimal units, CurrencyType orderCurrency, CurrencyType paymentCurrency);

    String marketSell(BigDecimal units, CurrencyType orderCurrency, CurrencyType paymentCurrency);
}
