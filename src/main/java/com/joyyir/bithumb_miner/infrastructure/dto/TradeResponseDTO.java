package com.joyyir.bithumb_miner.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TradeResponseDTO {
    @JsonProperty("status")
    private String status;

    @JsonProperty("order_id")
    private String orderId;
}
