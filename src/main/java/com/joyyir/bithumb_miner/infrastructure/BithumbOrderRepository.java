package com.joyyir.bithumb_miner.infrastructure;

import com.joyyir.bithumb_miner.domain.CurrencyType;
import com.joyyir.bithumb_miner.domain.OrderDetail;
import com.joyyir.bithumb_miner.domain.OrderRepository;
import com.joyyir.bithumb_miner.domain.OrderStatus;
import com.joyyir.bithumb_miner.infrastructure.dto.OrderDetailResponseDTO;
import com.joyyir.bithumb_miner.infrastructure.util.BithumbUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Repository
public class BithumbOrderRepository implements OrderRepository {
    private static final String URL_PREFIX = "https://api.bithumb.com";
    private static final String END_POINT_ORDERS = "/info/order_detail";
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String secretKey;

    public BithumbOrderRepository(RestTemplate restTemplate,
                                  @Value("${connect-key}") String apiKey,
                                  @Value("${secret-key}") String secretKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Override
    public OrderDetail getOrderDetail(String orderId, CurrencyType orderCurrency) {
        final String endpoint = END_POINT_ORDERS;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("order_id", orderId);
        params.add("order_currency", orderCurrency.getCode());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, BithumbUtil.privateApiHeader(apiKey, secretKey, endpoint, params));
        ResponseEntity<OrderDetailResponseDTO> response = restTemplate.postForEntity(URL_PREFIX + endpoint, entity, OrderDetailResponseDTO.class);
        if (response.getBody() == null || !"0000".equals(response.getBody().getStatus())) {
            throw new RuntimeException("getOrderDetail failed! status:" + (response.getBody() != null ? response.getBody().getStatus() : null));
        }
        OrderDetailResponseDTO.OrderDetailDataDTO orderDetail = response.getBody().getData();
        return new OrderDetail("Completed".equals(orderDetail.getOrderStatus()) ? OrderStatus.COMPLETED : OrderStatus.UNKNOWN,
                               orderDetail.getOrderQty());
    }
}
