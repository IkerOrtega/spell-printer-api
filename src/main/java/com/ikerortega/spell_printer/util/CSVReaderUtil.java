package com.ikerortega.spell_printer.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.util.*;

public class CSVReaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(CSVReaderUtil.class);

    public static <T> List<T> readCSV(String path, Class<T> objectclass, String filter) {
        try {
            return new CsvToBeanBuilder<T>(new FileReader(ResourceUtils.getFile(path)))
                    .withType(objectclass).withFilter(line -> {
                        if(filter != null && !filter.isEmpty()) {
                            return Arrays.stream(line).anyMatch(value -> value.toLowerCase().contains(filter.toLowerCase()));
                        } else {
                            return true;
                        }
                    }).withSeparator(';').build().parse();

        } catch (Exception e) {
            logger.error("Error reading CSV: ", e);
            return new ArrayList<>();
        }
    }

    public static  Map<String, List<String>> readCSVToMap(String path, String filter) {
        Map<String, List<String>> columnsMap = new HashMap<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(ResourceUtils.getFile(path))).withCSVParser(parser).build()){

            // headers
            String[] headers = csvReader.readNext();
            if(headers != null) {
                for(String header : headers) {
                    columnsMap.put(header, new ArrayList<>());
                }
                String[] row;
                while((row = csvReader.readNext()) != null)
                {
                    if(filter == null || filter.isEmpty() || Arrays.stream(row).anyMatch(value -> value.toLowerCase().contains(filter.toLowerCase()))) {
                        for(int i = 0; i < row.length; i++) {
                            columnsMap.get(headers[i]).add(row[i]);
                        }
                    }
                }
            } else {
                logger.error("Error reading CSV: No headers found");
            }
        } catch (Exception e) {
            logger.error("Error reading CSV: ", e);
        }
        return columnsMap;
    }
}
