package com.joyyir.bithumb_miner.domain;

public interface OrderBookRepository {
    OrderBook getOrderBook(CurrencyType orderCurrency, CurrencyType paymentCurrency);
}
