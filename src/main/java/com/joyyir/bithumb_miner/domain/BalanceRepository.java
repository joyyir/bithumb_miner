package com.joyyir.bithumb_miner.domain;

public interface BalanceRepository {
    Balance getBalance(CurrencyType currency);
}
