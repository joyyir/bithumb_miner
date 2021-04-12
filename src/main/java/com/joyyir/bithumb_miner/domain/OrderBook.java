package com.joyyir.bithumb_miner.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class OrderBook {
    private List<OrderBook.OrderBookItem> bids; // 매수. 비싼게 리스트 앞에 있음
    private List<OrderBook.OrderBookItem> asks; // 매도. 싼게 리스트 앞에 있음

    @AllArgsConstructor
    @Getter
    public static class OrderBookItem {
        private BigDecimal quantity;
        private BigDecimal price;
    }
}
