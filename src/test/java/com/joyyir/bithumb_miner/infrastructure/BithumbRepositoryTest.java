package com.joyyir.bithumb_miner.infrastructure;

import com.joyyir.bithumb_miner.domain.Balance;
import com.joyyir.bithumb_miner.domain.CurrencyType;
import com.joyyir.bithumb_miner.domain.OrderBook;
import com.joyyir.bithumb_miner.domain.OrderDetail;
import com.joyyir.bithumb_miner.domain.PlaceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BithumbRepositoryTest {
    @Autowired
    BithumbTradeRepository tradeRepository;

    @Autowired
    BithumbBalanceRepository balanceRepository;

    @Autowired
    BithumbOrderRepository orderRepository;

    @Autowired
    BithumbOrderBookRepository orderBookRepository;

    @Test
    void testBigDecimal() {
        BigDecimal val = BigDecimal.valueOf(3.1415);
        assertEquals("3.15", val.setScale(2, RoundingMode.CEILING).toString());
        assertEquals("3.14", val.setScale(2, RoundingMode.FLOOR).toString());
        assertEquals("3.142", val.setScale(3, RoundingMode.UP).toString());
        assertEquals("3.141", val.setScale(3, RoundingMode.DOWN).toString());
        assertEquals("3.142", val.setScale(3, RoundingMode.HALF_UP).toString());
        assertEquals("3.141", val.setScale(3, RoundingMode.HALF_DOWN).toString());
        assertEquals("3.142", val.setScale(3, RoundingMode.HALF_EVEN).toString());

        BigDecimal val2 = BigDecimal.valueOf(3.0000);
        assertEquals("3.000", val2.setScale(3, RoundingMode.HALF_EVEN).toString());
    }

    @Test
    void marketSell() {
        String orderId = tradeRepository.marketSell(new BigDecimal("10"), CurrencyType.XRP, CurrencyType.KRW);
        System.out.println(orderId);
        assertTrue(!orderId.isEmpty()); // C0101000000333534992
    }

    @Test
    void getBalance() {
        Balance balance = balanceRepository.getBalance(CurrencyType.KRW);
        System.out.println(balance);
    }

    @Test
    void getOrderDetail() {
        OrderDetail detail = orderRepository.getOrderDetail("C0101000000336227623", CurrencyType.BTC);
        System.out.println(detail);
    }

    @Test
    void getOrderBook() {
        OrderBook orderBook = orderBookRepository.getOrderBook(CurrencyType.BTC, CurrencyType.KRW);
        System.out.println(orderBook);
    }

    @Test
    void place() {
        tradeRepository.place(PlaceType.BUY, CurrencyType.XRP, CurrencyType.KRW, 1650, new BigDecimal("8"));
//        tradeRepository.place(PlaceType.SELL, CurrencyType.XRP, CurrencyType.KRW, 1500, new BigDecimal("10"));
    }
}