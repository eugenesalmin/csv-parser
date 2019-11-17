package com.test.csvparser.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.test.csvparser.model.CsvStat;
import com.test.csvparser.service.CsvService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class CsvServiceImpl implements CsvService {

    private static List<String[]> readCsvLines(Reader reader) throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();
        reader.close();
        return list;
    }

    private static CsvStat parseCsv(List<String[]> csvContent, String filePath) {

         Map<Long, Long> emptyCellsStat = new HashMap<>();
        CsvStat.CsvStatBuilder builder = CsvStat.builder().fileName(filePath);

        if (csvContent == null || csvContent.size() == 0)
            return builder.rowCount(0).build();

        builder.rowCount(csvContent.size()).columns(Arrays.asList(csvContent.get(0)));

        for (int i = 1; i < csvContent.size(); i++) {
            for (int j = 0; j < csvContent.get(0).length; j++) {
                String sell;
                try {
                    sell = csvContent.get(i)[j];
                } catch (Exception e) {
                    emptyCellsStat.put(new Long(j), getEmptyCellsPerColumnAmount(emptyCellsStat, j)); //in case we have a column and no values at all
                    continue;
                }
                if (StringUtils.isEmpty(sell)) { //space symbol is also a valid value for a cell
                    emptyCellsStat.put(new Long(j), getEmptyCellsPerColumnAmount(emptyCellsStat, j));
                }
            }
        }
        return builder.emptyCellsStat(emptyCellsStat).build();
    }

    private static Long getEmptyCellsPerColumnAmount(Map<Long, Long> emptyStat, long j) {

        Long emptyEntriesNum = emptyStat.get(j);
        if (emptyEntriesNum == null) {
            return 1L;
        }
        return emptyEntriesNum + 1;
    }

    @Override
    public List<CsvStat> analyzeCSVs(String folderPathName) {
        List<String> csvList = getCSVfromFolder(folderPathName);
        List<CsvStat> stats = new ArrayList<>();

        if (csvList == null) {
            return null;
        }

        for (String csv : csvList) {
            try {
                Reader reader = Files.newBufferedReader(Paths.get(csv));
                List<String[]> csvLines  = readCsvLines(reader);
                stats.add(parseCsv(csvLines, csv));
            } catch (CsvException | IOException e) {
                log.error("Error on reading CSV: {}", csv);
                e.printStackTrace();
            }
        }
        return stats;
    }


    private List<String> getCSVfromFolder(String folderPathName) {
        try (Stream<Path> walk = Files.walk(Paths.get(folderPathName))) {

            return walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".csv")).collect(Collectors.toList());


        } catch (IOException e) {
            log.error("Error in reading folder provided: {}", folderPathName);
            e.printStackTrace();
        }
        return null;
    }
}
