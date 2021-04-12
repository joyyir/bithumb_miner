package com.joyyir.bithumb_miner.domain;

public interface OrderRepository {
    OrderDetail getOrderDetail(String orderId, CurrencyType orderCurrency);
}
