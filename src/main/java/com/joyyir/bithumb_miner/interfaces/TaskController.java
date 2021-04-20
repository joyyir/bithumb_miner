package com.joyyir.bithumb_miner.interfaces;

import com.joyyir.bithumb_miner.application.MiningService;
import com.joyyir.bithumb_miner.domain.CurrencyType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class TaskController {
    private final MiningService miningService;

    public TaskController(MiningService miningService) {
        this.miningService = miningService;
    }

    @PostMapping(path = "/mining")
    public String mining(String currencyType, Integer cycleCount, BigDecimal maxKrw) {
        try {
            CurrencyType buyCurrency = CurrencyType.findByCode(currencyType);
            miningService.mining(buyCurrency, maxKrw, cycleCount);
        } catch (Exception e) {
            return "오류 발생: " + e.getMessage();
        }
        return "성공";
    }
}
