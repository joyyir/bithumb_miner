package com.joyyir.bithumb_miner.infrastructure;

import com.joyyir.bithumb_miner.domain.CurrencyType;
import com.joyyir.bithumb_miner.domain.OrderBook;
import com.joyyir.bithumb_miner.domain.OrderBookRepository;
import com.joyyir.bithumb_miner.infrastructure.dto.OrderBookResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BithumbOrderBookRepository implements OrderBookRepository {
    private static final String URL_PREFIX = "https://api.bithumb.com";
    private static final String END_POINT_ORDER_BOOK = "/public/orderbook/{order_currency}_{payment_currency}";
    private final RestTemplate restTemplate;

    public BithumbOrderBookRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OrderBook getOrderBook(CurrencyType orderCurrency, CurrencyType paymentCurrency) {
        final String endpoint = END_POINT_ORDER_BOOK;
        ResponseEntity<OrderBookResponseDTO> response = restTemplate.getForEntity(URL_PREFIX + endpoint.replace("{order_currency}", orderCurrency.getCode())
                                                                                                       .replace("{payment_currency}", paymentCurrency.getCode()),
                                                                                  OrderBookResponseDTO.class);
        OrderBookResponseDTO responseBody = response.getBody();
        if (responseBody == null || !"0000".equals(responseBody.getStatus())) {
            throw new RuntimeException("getOrderBook failed! status:" + (responseBody != null ? responseBody.getStatus() : null));
        }
        return new OrderBook(convert(responseBody.getData().getBids()),
                             convert(responseBody.getData().getAsks()));
    }

    private List<OrderBook.OrderBookItem> convert(List<OrderBookResponseDTO.OrderBookItemDTO> list) {
        return list.stream()
                   .map(x -> new OrderBook.OrderBookItem(x.getQuantity(), x.getPrice()))
                   .collect(Collectors.toList());
    }
}
