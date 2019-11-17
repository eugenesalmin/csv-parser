package com.test.csvparser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CsvStat {

    private final String fileName;

    private final long rowCount;

    @Builder.Default
    private final List<String> columns = new ArrayList<>();

    @Builder.Default
    private final Map<Long, Long> emptyCellsStat = new HashMap<>();
}
