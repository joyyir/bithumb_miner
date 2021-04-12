package com.joyyir.bithumb_miner.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderDetail {
    private OrderStatus orderStatus;
    private BigDecimal orderQty;
}
