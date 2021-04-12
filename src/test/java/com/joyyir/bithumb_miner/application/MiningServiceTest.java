package com.joyyir.bithumb_miner.application;

import com.joyyir.bithumb_miner.domain.OrderBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiningServiceTest {
    private MiningService service;

    @BeforeEach
    void setUp() {
        this.service = new MiningService(null, null, null, null);
    }

    @Test
    void getAvailableQuantity_case_1() {
        BigDecimal availableQuantity = this.service.getAvailableQuantity(BigDecimal.valueOf(500), List.of(
            new OrderBook.OrderBookItem(BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
            new OrderBook.OrderBookItem(BigDecimal.valueOf(10), BigDecimal.valueOf(20)),
            new OrderBook.OrderBookItem(BigDecimal.valueOf(10), BigDecimal.valueOf(30))
        ));
        assertEquals(new BigDecimal("26.66666666"), availableQuantity);
    }

    @Test
    void getAvailableQuantity_case_2() {
        BigDecimal availableQuantity = this.service.getAvailableQuantity(BigDecimal.valueOf(50), List.of(
            new OrderBook.OrderBookItem(BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
            new OrderBook.OrderBookItem(BigDecimal.valueOf(10), BigDecimal.valueOf(20)),
            new OrderBook.OrderBookItem(BigDecimal.valueOf(10), BigDecimal.valueOf(30))
        ));
        assertEquals(new BigDecimal("5.00000000"), availableQuantity);
    }
}