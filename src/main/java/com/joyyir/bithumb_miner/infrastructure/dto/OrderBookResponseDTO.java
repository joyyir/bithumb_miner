package com.joyyir.bithumb_miner.infrastructure.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderBookResponseDTO {
    private String status;
    private OrderBookDataDTO data;

    @Data
    public static class OrderBookDataDTO {
        private Long timestamp;
        private List<OrderBookItemDTO> bids;
        private List<OrderBookItemDTO> asks;
    }

    @Data
    public static class OrderBookItemDTO {
        private BigDecimal quantity;
        private BigDecimal price;
    }
}
