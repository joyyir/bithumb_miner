package com.joyyir.bithumb_miner.infrastructure.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BalanceResponseDTO {
    private String status;
    private Map<String, String> data;
}
