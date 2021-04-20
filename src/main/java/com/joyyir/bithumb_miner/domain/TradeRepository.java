package com.joyyir.bithumb_miner.domain;

import java.math.BigDecimal;

public interface TradeRepository {
    String marketSell(BigDecimal units, CurrencyType orderCurrency, CurrencyType paymentCurrency);

    String place(PlaceType placeType, CurrencyType orderCurrency, CurrencyType paymentCurrency, Integer price, BigDecimal units);
}
