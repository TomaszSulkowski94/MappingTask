package com.mapping.task.service.impl;

import com.mapping.task.service.FileLoaderService;
import com.mapping.task.service.ValidationService;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class FileLoaderServiceImpl implements FileLoaderService {

    @Value("${mapping.file.name}")
    private String mappingFile;

    private final ValidationService validationService;

    @Autowired
    public FileLoaderServiceImpl(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public List<Record> loadMappingFile() {
        log.info("Loading mapping file from {}", mappingFile);
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(mappingFile);
        log.info("Parsing input file");
        List<Record> records = new CsvParser(new CsvParserSettings()).parseAllRecords(is);
        validationService.validateInputCsvFile(records);
        return records;
    }
}
