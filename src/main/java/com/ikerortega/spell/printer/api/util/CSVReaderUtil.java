package com.ikerortega.spell.printer.api.util;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(CSVReaderUtil.class);

    public static <T> List<T> readCSV(String path, Class<T> objectclass, String filter) {
        try {
            return new CsvToBeanBuilder<T>(new FileReader(ResourceUtils.getFile(path)))
                    .withType(objectclass).withFilter(line -> {
                        if(filter != null && !filter.isEmpty()) {
                            return Arrays.stream(line).anyMatch(val -> val.toLowerCase().contains(filter.toLowerCase()));
                        } else {
                            return true;
                        }
                    }).withSeparator(';').build().parse();

        } catch (Exception e) {
            logger.error("Error reading CSV: ", e);
            return new ArrayList<>();
        }
    }
}
