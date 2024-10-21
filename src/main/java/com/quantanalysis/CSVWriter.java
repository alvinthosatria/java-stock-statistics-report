package com.quantanalysis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVWriter {
    private static CSVWriter instance;

    private CSVWriter() {
    }

    public static CSVWriter getInstance() {
        if (instance == null) {
            instance = new CSVWriter();
        }
        return instance;
    }

    public void writeStatisticsToCSV(String filePath, Map<String, StockStatistics> statistics) {
        try (FileWriter out = new FileWriter(filePath);
                CSVPrinter printer = new CSVPrinter(out, CSVFormat.Builder.create()
                        .setHeader("StockId", "MeanTimeBetweenTrades(seconds)", "MedianTimeBetweenTrades(seconds)",
                                "MeanTimeBetweenTickChanges(seconds)", "MedianTimeBetweenTickChanges(seconds)",
                                "LongestTimeBetweenTrades(seconds)", "LongestTimeBetweenTickChanges(seconds)",
                                "MeanBidAskSpread", "MedianBidAskSpread", "RoundNumberEffect")
                        .build())) {

            for (Map.Entry<String, StockStatistics> entry : statistics.entrySet()) {
                String stockId = entry.getKey();
                StockStatistics stat = entry.getValue();

                printer.printRecord(
                        stockId,
                        stat.getMeanTimeBetweenTrades(),
                        stat.getMedianTimeBetweenTrades(),
                        stat.getMeanTimeBetweenTickChanges(),
                        stat.getMedianTimeBetweenTickChanges(),
                        stat.getLongestTimeBetweenTrades(),
                        stat.getLongestTimeBetweenTickChanges(),
                        stat.getMeanBidAskSpread(),
                        stat.getMedianBidAskSpread(),
                        stat.getRoundNumberEffect());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}