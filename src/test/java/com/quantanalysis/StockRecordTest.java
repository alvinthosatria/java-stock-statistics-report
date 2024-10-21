package com.quantanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class StockRecordTest {
    /**
     * Rigorous Test :-)
     */
    private static final double DELTA = 1e-15;

    @Test
    void testStockRecordInitialization() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 9, 12, 0);
        StockRecord stockRecord = new StockRecord("STOCK_ID", 100.0, 105.0, 102.5, 1, dateTime);

        assertEquals("STOCK_ID", stockRecord.getStockId());
        assertEquals(100.0, stockRecord.getBidPrice(), DELTA);
        assertEquals(105.0, stockRecord.getAskPrice(), DELTA);
        assertEquals(102.5, stockRecord.getTradePrice(), DELTA);
        assertEquals(1, stockRecord.getUpdateType());
        assertEquals(dateTime, stockRecord.getDateTime());
    }
}
