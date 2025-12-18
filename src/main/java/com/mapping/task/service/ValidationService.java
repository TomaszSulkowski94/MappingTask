package com.mapping.task.service;

import com.univocity.parsers.common.record.Record;

import java.util.List;

public interface ValidationService {

    void validateInputParameters(String category, String numberLists);

    void validateInputCsvFile(List<Record> records);
}
