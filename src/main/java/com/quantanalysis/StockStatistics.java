package com.quantanalysis;

import java.time.LocalDateTime;
import java.util.*;

public class StockStatistics {
    private final ArrayList<Long> tradeTimes = new ArrayList<>();
    private final ArrayList<Long> tickChanges = new ArrayList<>();
    private final ArrayList<Double> bidAskSpreads = new ArrayList<>();
    private final ArrayList<Double> tradePrices = new ArrayList<>();

    private LocalDateTime lastTradeTime;
    private LocalDateTime lastTickTime;

    public void update(StockRecord stockRecord) {
        // 1=Trade; 2= Change to Bid (Px or Vol); 3=Change to Ask (Px or Vol)
        if (stockRecord.getUpdateType() == 1) {
            if (lastTradeTime != null) {
                tradeTimes.add(java.time.Duration.between(lastTradeTime, stockRecord.getDateTime()).getSeconds());
            }
            lastTradeTime = stockRecord.getDateTime();
            tradePrices.add(stockRecord.getTradePrice());
        } else {
            if (lastTickTime != null) {
                tickChanges.add(java.time.Duration.between(lastTickTime, stockRecord.getDateTime()).getSeconds());
            }
            lastTickTime = stockRecord.getDateTime();
        }

        double spread = stockRecord.getAskPrice() - stockRecord.getBidPrice();
        bidAskSpreads.add(spread);
    }

    public double getMeanTimeBetweenTrades() {
        return mean(tradeTimes);
    }

    public double getMedianTimeBetweenTrades() {
        return median(tradeTimes);
    }

    public long getLongestTimeBetweenTrades() {
        return max(tradeTimes);
    }

    public double getMeanTimeBetweenTickChanges() {
        return mean(tickChanges);
    }

    public double getMedianTimeBetweenTickChanges() {
        return median(tickChanges);
    }

    public long getLongestTimeBetweenTickChanges() {
        return max(tickChanges);
    }

    public double getMeanBidAskSpread() {
        return mean(bidAskSpreads);
    }

    public double getMedianBidAskSpread() {
        return median(bidAskSpreads);
    }

    public double getRoundNumberEffect() {
        return calculateRoundNumberEffect(tradePrices);
    }

    private double calculateRoundNumberEffect(ArrayList<Double> prices) {
        long roundCount = prices.stream()
                .filter(price -> Math.round(price) % 10 == 0)
                .count();
        return (double) roundCount / prices.size();
    }

    private double mean(List<? extends Number> list) {
        return list.stream().mapToDouble(Number::doubleValue).average().orElse(0.0);
    }

    private double median(List<? extends Number> list) {
        int size = list.size();
        if (size == 0)
            return 0.0;
        List<? extends Number> sorted = new ArrayList<>(list);
        Collections.sort(sorted, Comparator.comparingDouble(Number::doubleValue));
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1).doubleValue() + sorted.get(size / 2).doubleValue()) / 2.0;
        } else {
            return sorted.get((size / 2)).doubleValue();
        }
    }

    private long max(List<Long> list) {
        return list.stream().max(Long::compare).orElse(0L);
    }
}