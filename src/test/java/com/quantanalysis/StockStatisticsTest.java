package com.quantanalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class StockStatisticsTest {
    /**
     * Rigorous Test :-)
     */
    private StockStatistics statistics;
    private static final double DELTA = 1e-15;

    @BeforeEach
    public void setUp() {
        statistics = new StockStatistics();
    }

    @Test
    void testMeanBidAskSpread() {
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 102.5, 1, LocalDateTime.now()));
        statistics.update(new StockRecord("STOCK_ID", 102.0, 105.0, 102.5, 1, LocalDateTime.now().plusSeconds(10)));

        assertEquals(4.0, statistics.getMeanBidAskSpread(), DELTA);
    }

    @Test
    void testMeanTimeBetweenTrades() {
        LocalDateTime now = LocalDateTime.now();
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 102.5, 1, now));
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 103.5, 1, now.plusSeconds(10)));

        assertEquals(10.0, statistics.getMeanTimeBetweenTrades(), 0.01);
    }

    @Test
    void testMedianTimeBetweenTrades() {
        LocalDateTime now = LocalDateTime.now();
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 102.5, 1, now));
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 103.5, 1, now.plusSeconds(10)));
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 104.5, 1, now.plusSeconds(20)));

        assertEquals(10.0, statistics.getMedianTimeBetweenTrades(), 0.01);
    }

    @Test
    void testLongestTimeBetweenTrades() {
        LocalDateTime now = LocalDateTime.now();
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 102.5, 1, now));
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 103.5, 1, now.plusSeconds(10)));
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 104.5, 1, now.plusSeconds(30)));

        assertEquals(20, statistics.getLongestTimeBetweenTrades());
    }

    @Test
    void testMedianBidAskSpread() {
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 102.5, 1, LocalDateTime.now()));
        statistics.update(new StockRecord("STOCK_ID", 101.0, 106.0, 103.5, 1, LocalDateTime.now()));
        statistics.update(new StockRecord("STOCK_ID", 102.0, 107.0, 104.5, 1, LocalDateTime.now()));

        assertEquals(5.0, statistics.getMedianBidAskSpread(), 0.01);
    }

    @Test
    void testRoundNumberEffect() {
        statistics.update(new StockRecord("STOCK_ID", 100.0, 105.0, 100.0, 1, LocalDateTime.now()));
        statistics.update(new StockRecord("STOCK_ID", 101.0, 106.0, 103.5, 1, LocalDateTime.now()));
        statistics.update(new StockRecord("STOCK_ID", 102.0, 107.0, 110.0, 1, LocalDateTime.now()));

        assertEquals(0.67, statistics.getRoundNumberEffect(), 0.01);
    }
}
