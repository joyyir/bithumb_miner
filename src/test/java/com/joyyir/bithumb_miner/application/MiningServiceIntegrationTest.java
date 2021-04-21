package com.joyyir.bithumb_miner.application;

import com.joyyir.bithumb_miner.domain.CurrencyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class MiningServiceIntegrationTest {

    @Autowired
    private MiningService miningService;

    @Test
    void mining() {
        this.miningService.mining(CurrencyType.BTC, new BigDecimal("7923"), 3);
    }

    @Test
    void marketSell() {
        this.miningService.marketSell(CurrencyType.BTC, new BigDecimal("0.0004"));
    }
}