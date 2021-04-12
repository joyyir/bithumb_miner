package com.joyyir.bithumb_miner.infrastructure;

import com.joyyir.bithumb_miner.domain.CurrencyType;
import com.joyyir.bithumb_miner.domain.TradeRepository;
import com.joyyir.bithumb_miner.infrastructure.dto.TradeResponseDTO;
import com.joyyir.bithumb_miner.infrastructure.util.Encryptor;
import com.joyyir.bithumb_miner.infrastructure.util.HTTPUtil;
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
    private static final String END_POINT_MARKET_BUY = "/trade/market_buy";
    private static final String END_POINT_MARKET_SELL = "/trade/market_sell";
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
    public String marketBuy(BigDecimal units, CurrencyType orderCurrency, CurrencyType paymentCurrency) {
        final String endpoint = END_POINT_MARKET_BUY;
        final String nonce = String.valueOf(System.currentTimeMillis());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("units", units.setScale(4, RoundingMode.FLOOR).toString());
        params.add("order_currency", orderCurrency.getCode());
        params.add("payment_currency", paymentCurrency.getCode());

        String strParams = HTTPUtil.paramsBuilder(params);
        String encodedParams = HTTPUtil.encodeURIComponent(strParams);

        String str = endpoint + ";" + encodedParams + ";" + nonce;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Api-Key", apiKey);
        headers.add("Api-Sign", Encryptor.getHmacSha512(secretKey, str, Encryptor.EncodeType.BASE64));
        headers.add("Api-Nonce", nonce);
        headers.add("api-client-type", "2");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<TradeResponseDTO> response = restTemplate.postForEntity(URL_PREFIX + endpoint, entity, TradeResponseDTO.class);
        if (response.getBody() == null || !"0000".equals(response.getBody().getStatus())) {
            throw new RuntimeException("marketBuy failed! status:" + (response.getBody() != null ? response.getBody().getStatus() : null));
        }
        return response.getBody().getOrderId();
    }

    @Override
    public String marketSell(BigDecimal units, CurrencyType orderCurrency, CurrencyType paymentCurrency) {
        final String endpoint = END_POINT_MARKET_SELL;
        final String nonce = String.valueOf(System.currentTimeMillis());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("units", units.setScale(4, RoundingMode.FLOOR).toString());
        params.add("order_currency", orderCurrency.getCode());
        params.add("payment_currency", paymentCurrency.getCode());

        String strParams = HTTPUtil.paramsBuilder(params);
        String encodedParams = HTTPUtil.encodeURIComponent(strParams);

        String str = endpoint + ";" + encodedParams + ";" + nonce;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Api-Key", apiKey);
        headers.add("Api-Sign", Encryptor.getHmacSha512(secretKey, str, Encryptor.EncodeType.BASE64));
        headers.add("Api-Nonce", nonce);
        headers.add("api-client-type", "2");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<TradeResponseDTO> response = restTemplate.postForEntity(URL_PREFIX + endpoint, entity, TradeResponseDTO.class);
        if (response.getBody() == null || !"0000".equals(response.getBody().getStatus())) {
            throw new RuntimeException("marketSell failed! status:" + (response.getBody() != null ? response.getBody().getStatus() : null));
        }
        return response.getBody().getOrderId();
    }
}
