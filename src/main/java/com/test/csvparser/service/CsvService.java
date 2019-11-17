package com.test.csvparser.service;

import com.test.csvparser.model.CsvStat;

import java.util.List;


public interface CsvService {
    List<CsvStat> analyzeCSVs(String folderPathName);
}
