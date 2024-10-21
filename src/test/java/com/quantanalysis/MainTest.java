package com.quantanalysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import org.mockito.Mockito;

import java.util.*;

class MainTest {
    /**
     * Rigorous Test :-)
     */
    private static final double DELTA = 1e-15;

    @Test
    void shouldParseCSVRecord() {
        DateTimeParser dateTimeParser = new DateTimeParser("yyyyMMdd HHmmss");
        CSVRecord csvRecord = Mockito.mock(CSVRecord.class);

        Mockito.when(csvRecord.get(0)).thenReturn("STOCK_ID");
        Mockito.when(csvRecord.get(2)).thenReturn("100.0");
        Mockito.when(csvRecord.get(3)).thenReturn("101.0");
        Mockito.when(csvRecord.get(4)).thenReturn("100.5");
        Mockito.when(csvRecord.get(8)).thenReturn("1");
        Mockito.when(csvRecord.get(10)).thenReturn("20231008");
        Mockito.when(csvRecord.get(11)).thenReturn("0");
        Mockito.when(csvRecord.get(14)).thenReturn("XT");

        StockRecord stockRecord = Main.parseCSVRecord(csvRecord, dateTimeParser);

        assertNotNull(stockRecord);
        assertEquals("STOCK_ID", stockRecord.getStockId());
        assertEquals(100.0, stockRecord.getBidPrice(), DELTA);
        assertEquals(101.0, stockRecord.getAskPrice(), DELTA);
        assertEquals(100.5, stockRecord.getTradePrice(), DELTA);
        assertEquals(1, stockRecord.getUpdateType(), DELTA);
        assertEquals(LocalDateTime.of(2023, 10, 8, 0, 0), stockRecord.getDateTime());
    }

    @Test
    void shouldParseCSVRecordWithExcludeRecords() {
        DateTimeParser dateTimeParser = new DateTimeParser("yyyyMMdd HHmmss");
        CSVRecord csvRecord = Mockito.mock(CSVRecord.class);

        Mockito.when(csvRecord.get(0)).thenReturn("STOCK_ID");
        Mockito.when(csvRecord.get(2)).thenReturn("103.0");
        Mockito.when(csvRecord.get(3)).thenReturn("101.0");
        Mockito.when(csvRecord.get(4)).thenReturn("100.5");
        Mockito.when(csvRecord.get(8)).thenReturn("1");
        Mockito.when(csvRecord.get(10)).thenReturn("20231008");
        Mockito.when(csvRecord.get(11)).thenReturn("0");
        Mockito.when(csvRecord.get(14)).thenReturn("YT");

        StockRecord stockRecord = Main.parseCSVRecord(csvRecord, dateTimeParser);

        assertNull(stockRecord);
    }

    @Test
    void shouldParseCSVRecordAndReturnNull() {
        DateTimeParser dateTimeParser = new DateTimeParser("yyyyMMdd HHmmss");
        CSVRecord csvRecord = Mockito.mock(CSVRecord.class);

        Mockito.when(csvRecord.get(0)).thenReturn("STOCK_ID");
        Mockito.when(csvRecord.get(2)).thenReturn("102.0");
        Mockito.when(csvRecord.get(3)).thenReturn("101.0");
        Mockito.when(csvRecord.get(4)).thenReturn("100.5");
        Mockito.when(csvRecord.get(8)).thenReturn("1");
        Mockito.when(csvRecord.get(10)).thenReturn("20231008");
        Mockito.when(csvRecord.get(11)).thenReturn("0");
        Mockito.when(csvRecord.get(14)).thenReturn("XT");

        StockRecord stockRecord = Main.parseCSVRecord(csvRecord, dateTimeParser);

        assertNull(stockRecord);
    }

    @Test
    void shouldReadEmptyCSVAndReturnEmptyRecords() throws IOException {
        File tempFile = File.createTempFile("testread", ".csv");
        tempFile.deleteOnExit();

        ArrayList<StockRecord> records = Main.readCSV(tempFile.getAbsolutePath());

        assertNotNull(records);
        assertTrue(records.isEmpty());
        assertEquals(0, records.size());

        tempFile.delete();
    }

    @Test
    void shouldReadCSVWithNullParsedRecords() throws IOException {
        File tempFile = File.createTempFile("invalid", ".csv");
        tempFile.deleteOnExit();
    
        try (FileWriter writer = new FileWriter(tempFile)) {
            // This is invalid auction day (bidPrice > askPrice)
            writer.write("BBHBEAT Index,61,121.4,125.4,123.4,1234,1234,1234,2,0,20150420,28266.0,0.0,0,@1\n");
        }
    
        ArrayList<StockRecord> records = Main.readCSV(tempFile.getAbsolutePath());
    
        assertNotNull(records);
        assertTrue(records.isEmpty());
    
        tempFile.delete();
    }

    @Test
    void shouldAnalyzeDataAndReturnStatistics() {
        ArrayList<StockRecord> records = new ArrayList<>();
        LocalDateTime ts = LocalDateTime.now();
        records.add(new StockRecord("STOCK_ID", 100.0, 101.0, 100.5, 1, ts));
        records.add(new StockRecord("STOCK_ID", 100.0, 101.0, 100.5, 2, ts.plusSeconds(10)));

        Map<String, StockStatistics> statistics = Main.analyzeData(records);

        assertNotNull(statistics);
        assertTrue(statistics.containsKey("STOCK_ID"));
    }

    @Test
    void testWriteStatisticsToCSV() throws IOException {
        Map<String, StockStatistics> statisticsMap = new HashMap<>();

        StockStatistics stats = new StockStatistics();
        stats.update(new StockRecord("AAPL", 100.0, 105.0, 102.5, 1, LocalDateTime.now()));
        stats.update(new StockRecord("AAPL", 101.0, 103.0, 101.5, 1, LocalDateTime.now().plusSeconds(5)));
        statisticsMap.put("AAPL", stats);

        File tempFile = File.createTempFile("testwrite", ".csv");
        tempFile.deleteOnExit();

        Main.writeStatisticsToCSV(tempFile.getAbsolutePath(), statisticsMap);

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("StockId", "MeanTimeBetweenTrades(seconds)", "MedianTimeBetweenTrades(seconds)",
                        "MeanTimeBetweenTickChanges(seconds)", "MedianTimeBetweenTickChanges(seconds)",
                        "LongestTimeBetweenTrades(seconds)", "LongestTimeBetweenTickChanges(seconds)",
                        "MeanBidAskSpread", "MedianBidAskSpread", "RoundNumberEffect")
                .setSkipHeaderRecord(true)
                .build();

        try (FileReader reader = new FileReader(tempFile);
                CSVParser csvParser = new CSVParser(reader, format)) {

            for (CSVRecord csvRecord : csvParser) {
                assertEquals("AAPL", csvRecord.get("StockId"));
                assertEquals(5.0, Double.parseDouble(csvRecord.get("MeanTimeBetweenTrades(seconds)")));
                assertEquals(5.0, Double.parseDouble(csvRecord.get("MedianTimeBetweenTrades(seconds)")));
                assertEquals(0.0, Double.parseDouble(csvRecord.get("MeanTimeBetweenTickChanges(seconds)")));
                assertEquals(0.0, Double.parseDouble(csvRecord.get("MedianTimeBetweenTickChanges(seconds)")));
                assertEquals(5, Long.parseLong(csvRecord.get("LongestTimeBetweenTrades(seconds)")));
                assertEquals(0, Long.parseLong(csvRecord.get("LongestTimeBetweenTickChanges(seconds)")));
                assertEquals(3.5, Double.parseDouble(csvRecord.get("MeanBidAskSpread")));
                assertEquals(3.5, Double.parseDouble(csvRecord.get("MedianBidAskSpread")));
                assertEquals(0.0, Double.parseDouble(csvRecord.get("RoundNumberEffect")));
            }
        }

        assertTrue(tempFile.exists());
        tempFile.delete();
    }
}
