package com.joyyir.bithumb_miner.application;

import com.joyyir.bithumb_miner.domain.Balance;
import com.joyyir.bithumb_miner.domain.BalanceRepository;
import com.joyyir.bithumb_miner.domain.CurrencyType;
import com.joyyir.bithumb_miner.domain.OrderBook;
import com.joyyir.bithumb_miner.domain.OrderBookRepository;
import com.joyyir.bithumb_miner.domain.OrderDetail;
import com.joyyir.bithumb_miner.domain.OrderRepository;
import com.joyyir.bithumb_miner.domain.OrderStatus;
import com.joyyir.bithumb_miner.domain.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
public class MiningService {
    private static final BigDecimal TRADE_FEE_RATIO = new BigDecimal("0.003"); // 0.3% (실제 수수료 0.25% + 혹시 모를 상황 대비용 0.05%)
    private final TradeRepository tradeRepository;
    private final BalanceRepository balanceRepository;
    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;

    public MiningService(TradeRepository tradeRepository,
                         BalanceRepository balanceRepository,
                         OrderRepository orderRepository, OrderBookRepository orderBookRepository) {
        this.tradeRepository = tradeRepository;
        this.balanceRepository = balanceRepository;
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
    }

    public void mining(CurrencyType buyCurrency, BigDecimal maxAmountOfKrw) {
        Balance startKrwBalance = balanceRepository.getBalance(CurrencyType.KRW);
        for (int i = 0; i < 5; i++) {
            log.info("{}번째 사이클: 시작", i + 1);
            buyAndSellByMarketPrice(buyCurrency, maxAmountOfKrw);
            log.info("{}번째 사이클: 종료", i + 1);
        }
        Balance finishKrwBalance = balanceRepository.getBalance(CurrencyType.KRW);
        log.info("정상 종료. (최종 잔고: {}원. 차감된 금액: {}원)", finishKrwBalance.getTotal(), finishKrwBalance.getTotal().subtract(startKrwBalance.getTotal()));
    }

    private void buyAndSellByMarketPrice(CurrencyType buyCurrency, BigDecimal maxAmountOfKrw) {
        Balance krwBalance = balanceRepository.getBalance(CurrencyType.KRW);
        BigDecimal amountOfKrw = krwBalance.getAvailable().compareTo(maxAmountOfKrw) < 0 ? krwBalance.getAvailable() : maxAmountOfKrw;
        log.info("현재 거래 가능한 잔고: {}원, 실제 거래할 잔고: {}원", krwBalance.getAvailable(), amountOfKrw);

        OrderBook orderBook = orderBookRepository.getOrderBook(buyCurrency, CurrencyType.KRW);
        BigDecimal availableQuantity = getAvailableQuantity(amountOfKrw.multiply(BigDecimal.ONE.subtract(TRADE_FEE_RATIO)),
                                                            orderBook.getAsks());
        OrderDetail orderDetail = marketBuy(buyCurrency, availableQuantity); // 수수료 뺀 금액까지만 살 수 있다
        marketSell(buyCurrency, orderDetail.getOrderQty()); // 팔고 나서 수수료가 빠진다
    }

    private OrderDetail marketBuy(CurrencyType buyCurrency, BigDecimal quantity) {
        String orderId = tradeRepository.marketBuy(quantity, buyCurrency, CurrencyType.KRW);
        log.info("{}를 {}개 매수 주문을 올렸습니다.", buyCurrency.getCode(), quantity);
        log.info("주문이 완료될 때까지 대기합니다.");
        OrderDetail orderDetail = null;
        for (int i = 0; i < 10; i++) {
            orderDetail = orderRepository.getOrderDetail(orderId, buyCurrency);
            if (isOrderCompleted(orderDetail)) {
                break;
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {}
        }
        if (!isOrderCompleted(orderDetail)) {
            log.error("10초 동안에 주문이 완료되지 않았습니다. 빗썸 사이트에서 확인해주세요. 종료합니다.");
            throw new RuntimeException("시장가 매수 오류");
        }
        log.info("매수가 완료되었습니다. ({}개)", orderDetail.getOrderQty());
        return orderDetail;
    }

    public OrderDetail marketSell(CurrencyType sellCurrency, BigDecimal quantity) {
        String orderId = tradeRepository.marketSell(quantity, sellCurrency, CurrencyType.KRW);
        log.info("{}를 {}개 매도 주문을 올렸습니다.", sellCurrency.getCode(), quantity);
        log.info("주문이 완료될 때까지 대기합니다.");
        OrderDetail orderDetail = null;
        for (int i = 0; i < 10; i++) {
            orderDetail = orderRepository.getOrderDetail(orderId, sellCurrency);
            if (isOrderCompleted(orderDetail)) {
                break;
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {}
        }
        if (!isOrderCompleted(orderDetail)) {
            log.error("10초 동안에 주문이 완료되지 않았습니다. 빗썸 사이트에서 확인해주세요. 종료합니다.");
            throw new RuntimeException("시장가 매도 오류");
        }
        log.info("매도가 완료되었습니다. ({}개)", orderDetail.getOrderQty());
        return orderDetail;
    }

    public BigDecimal getAvailableQuantity(BigDecimal amountOfKrw, List<OrderBook.OrderBookItem> asks) {
        BigDecimal tempAmountOfKrw = new BigDecimal(amountOfKrw.toString());
        BigDecimal quantitySum = new BigDecimal(0);

        for (OrderBook.OrderBookItem ask : asks) {
            if (tempAmountOfKrw.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal amount = ask.getPrice().multiply(ask.getQuantity());
            if (tempAmountOfKrw.compareTo(amount) >= 0) { // tempAmountOfKrw >= amount
                quantitySum = quantitySum.add(ask.getQuantity());
                tempAmountOfKrw = tempAmountOfKrw.subtract(amount);
            } else { // tempAmountOfKrw < amount
                quantitySum = quantitySum.add(tempAmountOfKrw.divide(ask.getPrice(), 8, RoundingMode.FLOOR));
                tempAmountOfKrw = new BigDecimal(0);
            }
        }
        return quantitySum;
    }

    private boolean isOrderCompleted(OrderDetail orderDetail) {
        return orderDetail != null && OrderStatus.COMPLETED.equals(orderDetail.getOrderStatus());
    }
}
