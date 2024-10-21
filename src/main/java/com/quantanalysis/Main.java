package com.quantanalysis;

import io.github.cdimascio.dotenv.Dotenv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    private static Dotenv dotenv = Dotenv.configure().directory("/.env").ignoreIfMissing().load();

    public static void main(String[] args) {
        ArrayList<StockRecord> records = readCSV(dotenv.get("MY_READ_CSV_PATH"));
        Map<String, StockStatistics> statistics = analyzeData(records);
        writeStatisticsToCSV(statistics);
    }

    static StockRecord parseCSVRecord(CSVRecord csvRecord, DateTimeParser dateTimeParser) {
        String stockId = csvRecord.get(0);
        double bidPrice = Double.parseDouble(csvRecord.get(2));
        double askPrice = Double.parseDouble(csvRecord.get(3));
        double tradePrice = Double.parseDouble(csvRecord.get(4));
        int updateType = Integer.parseInt(csvRecord.get(8));
        String conditionCode = csvRecord.get(14);
        LocalDateTime dateTime = dateTimeParser.parse(csvRecord.get(10), csvRecord.get(11));

        if ((conditionCode.startsWith("XT") || conditionCode.isEmpty()) && bidPrice <= askPrice) {
            return new StockRecord(stockId, bidPrice, askPrice, tradePrice, updateType, dateTime);
        }
        return null;
    }

    static ArrayList<StockRecord> readCSV(String filePath) {
        ArrayList<StockRecord> csvRecords = new ArrayList<>();
        try (CSVParser parser = CSVFormat.DEFAULT.parse(new FileReader(Paths.get(filePath).toFile()))) {
            DateTimeParser dateTimeParser = new DateTimeParser("yyyyMMdd HHmmss");
            for (CSVRecord csvRecord : parser) {
                StockRecord stockRecord = parseCSVRecord(csvRecord, dateTimeParser);
                if (stockRecord != null) {
                    csvRecords.add(stockRecord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvRecords;
    }

    static Map<String, StockStatistics> analyzeData(ArrayList<StockRecord> stockRecords) {
        Map<String, StockStatistics> statisticsMap = new HashMap<>();

        for (StockRecord stockRecord : stockRecords) {
            statisticsMap.putIfAbsent(stockRecord.getStockId(), new StockStatistics());
            StockStatistics stats = statisticsMap.get(stockRecord.getStockId());
            stats.update(stockRecord);
        }

        return statisticsMap;
    }

    static void writeStatisticsToCSV(Map<String, StockStatistics> statistics) {
        CSVWriter writer = CSVWriter.getInstance();
        writer.writeStatisticsToCSV(dotenv.get("MY_OUTPUT_CSV_PATH"), statistics);
    }

    static void writeStatisticsToCSV(String outputPath, Map<String, StockStatistics> statistics) {
        CSVWriter writer = CSVWriter.getInstance();
        writer.writeStatisticsToCSV(outputPath, statistics);
    }
}