package com.joyyir.bithumb_miner.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponseDTO {
    private String status;
    private OrderDetailDataDTO data;

    @Data
    public static class OrderDetailDataDTO {
        @JsonProperty("order_status")
        private String orderStatus;

        @JsonProperty("order_qty")
        private BigDecimal orderQty;
    }
}
