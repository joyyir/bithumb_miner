package com.joyyir.bithumb_miner.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class Balance {
    private BigDecimal total;
    private BigDecimal inUse;
    private BigDecimal available;
}
