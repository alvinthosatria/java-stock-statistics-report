package com.quantanalysis;

import java.time.LocalDateTime;

public class StockRecord {
    private final String stockId;
    private final double bidPrice;
    private final double askPrice;
    private final double tradePrice;
    private final int updateType;
    private final LocalDateTime dateTime;

    public StockRecord(String stockId, double bidPrice, double askPrice, double tradePrice, int updateType,
            LocalDateTime dateTime) {
        this.stockId = stockId;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.tradePrice = tradePrice;
        this.updateType = updateType;
        this.dateTime = dateTime;
    }

    public String getStockId() {
        return stockId;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public int getUpdateType() {
        return updateType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}