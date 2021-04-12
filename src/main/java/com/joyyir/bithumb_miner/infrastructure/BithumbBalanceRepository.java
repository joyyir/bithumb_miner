package com.joyyir.bithumb_miner.infrastructure;

import com.joyyir.bithumb_miner.domain.Balance;
import com.joyyir.bithumb_miner.domain.BalanceRepository;
import com.joyyir.bithumb_miner.domain.CurrencyType;
import com.joyyir.bithumb_miner.infrastructure.dto.BalanceResponseDTO;
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
import java.util.Map;

@Repository
public class BithumbBalanceRepository implements BalanceRepository {
    private static final String URL_PREFIX = "https://api.bithumb.com";
    private static final String END_POINT_BALANCE = "/info/balance";
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String secretKey;

    public BithumbBalanceRepository(RestTemplate restTemplate,
                                    @Value("${connect-key}") String apiKey,
                                    @Value("${secret-key}") String secretKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Override
    public Balance getBalance(CurrencyType currency) {
        final String endpoint = END_POINT_BALANCE;
        final String nonce = String.valueOf(System.currentTimeMillis());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("currency", currency == CurrencyType.KRW ? CurrencyType.BTC.getCode() : currency.getCode());

        String strParams = HTTPUtil.paramsBuilder(params);
        String encodedParams = HTTPUtil.encodeURIComponent(strParams);

        String str = endpoint + ";" + encodedParams + ";" + nonce;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Api-Key", apiKey);
        headers.add("Api-Sign", Encryptor.getHmacSha512(secretKey, str, Encryptor.EncodeType.BASE64));
        headers.add("Api-Nonce", nonce);
        headers.add("api-client-type", "2");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<BalanceResponseDTO> response = restTemplate.postForEntity(URL_PREFIX + endpoint, entity, BalanceResponseDTO.class);
        if (response.getBody() == null || !"0000".equals(response.getBody().getStatus())) {
            throw new RuntimeException("getBalance failed! status:" + (response.getBody() != null ? response.getBody().getStatus() : null));
        }
        Map<String, String> data = response.getBody().getData();
        return new Balance(new BigDecimal(data.getOrDefault("total_" + currency.getCode().toLowerCase(), "0")),
                           new BigDecimal(data.getOrDefault("in_use_" + currency.getCode().toLowerCase(), "0")),
                           new BigDecimal(data.getOrDefault("available_" + currency.getCode().toLowerCase(), "0")));
    }
}
