package com.quantanalysis;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

class CSVWriterTest {
    /**
     * Rigorous Test :-)
     */
    private CSVWriter csvWriter;
    private Map<String, StockStatistics> statisticsMap;

    @BeforeEach
    void setUp() {
        csvWriter = CSVWriter.getInstance();
        statisticsMap = new HashMap<>();

        StockStatistics stats = new StockStatistics();
        stats.update(new StockRecord("AAPL", 100.0, 105.0, 102.5, 1, LocalDateTime.now()));
        stats.update(new StockRecord("AAPL", 101.0, 103.0, 101.5, 1, LocalDateTime.now().plusSeconds(5)));
        statisticsMap.put("AAPL", stats);
    }

    @Test
    void testWriteStatisticsToCSV() throws IOException {
        String filePath = "test_statistics.csv";

        csvWriter.writeStatisticsToCSV(filePath, statisticsMap);

        File file = new File(filePath);
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("StockId", "MeanTimeBetweenTrades(seconds)", "MedianTimeBetweenTrades(seconds)",
                        "MeanTimeBetweenTickChanges(seconds)", "MedianTimeBetweenTickChanges(seconds)",
                        "LongestTimeBetweenTrades(seconds)", "LongestTimeBetweenTickChanges(seconds)",
                        "MeanBidAskSpread", "MedianBidAskSpread", "RoundNumberEffect")
                .setSkipHeaderRecord(true)
                .build();
        try (FileReader reader = new FileReader(file);
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

        file.delete();
    }
}
