package com.joyyir.bithumb_miner.infrastructure;

import com.joyyir.bithumb_miner.domain.CurrencyType;
import com.joyyir.bithumb_miner.domain.PlaceType;
import com.joyyir.bithumb_miner.domain.TradeRepository;
import com.joyyir.bithumb_miner.infrastructure.dto.TradeResponseDTO;
import com.joyyir.bithumb_miner.infrastructure.util.BithumbUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Repository
public class BithumbTradeRepository implements TradeRepository {
    private static final String URL_PREFIX = "https://api.bithumb.com";
    private static final String END_POINT_MARKET_SELL = "/trade/market_sell";
    private static final String END_POINT_PLACE = "/trade/place";
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String secretKey;

    public BithumbTradeRepository(RestTemplate restTemplate,
                                  @Value("${connect-key}") String apiKey,
                                  @Value("${secret-key}") String secretKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Override
    public String marketSell(BigDecimal units, CurrencyType orderCurrency, CurrencyType paymentCurrency) {
        final String endpoint = END_POINT_MARKET_SELL;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("units", units.setScale(4, RoundingMode.FLOOR).toString());
        params.add("order_currency", orderCurrency.getCode());
        params.add("payment_currency", paymentCurrency.getCode());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, BithumbUtil.privateApiHeader(apiKey, secretKey, endpoint, params));
        ResponseEntity<TradeResponseDTO> response = restTemplate.postForEntity(URL_PREFIX + endpoint, entity, TradeResponseDTO.class);
        if (response.getBody() == null || !"0000".equals(response.getBody().getStatus())) {
            throw new RuntimeException("marketSell failed! status:" + (response.getBody() != null ? response.getBody().getStatus() : null)
                                           + ", message: " + (response.getBody() != null ? response.getBody().getMessage() : null));
        }
        return response.getBody().getOrderId();
    }

    @Override
    public String place(PlaceType placeType, CurrencyType orderCurrency, CurrencyType paymentCurrency, Integer price, BigDecimal units) {
        final String endpoint = END_POINT_PLACE;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("units", units.setScale(4, RoundingMode.FLOOR).toString());
        params.add("price", price.toString());
        params.add("type", PlaceType.SELL == placeType ? "ask" : "bid");
        params.add("order_currency", orderCurrency.getCode());
        params.add("payment_currency", paymentCurrency.getCode());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, BithumbUtil.privateApiHeader(apiKey, secretKey, endpoint, params));
        ResponseEntity<TradeResponseDTO> response = restTemplate.postForEntity(URL_PREFIX + endpoint, entity, TradeResponseDTO.class);
        if (response.getBody() == null || !"0000".equals(response.getBody().getStatus())) {
            throw new RuntimeException("place failed! status:" + (response.getBody() != null ? response.getBody().getStatus() : null)
                                           + ", message: " + (response.getBody() != null ? response.getBody().getMessage() : null));
        }
        return response.getBody().getOrderId();
    }
}
