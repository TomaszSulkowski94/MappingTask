package com.mapping.task.service.impl;


import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.junit.jupiter.api.Test;
import utils.ValidatingFileException;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceImplTest {

    private final ValidationServiceImpl validationService = new ValidationServiceImpl();

    @Test
    void validateInputParametersShouldPass() {
        //given
        String category = "Animals";
        String numberLists = "1,2,3";
        //when
        validationService.validateInputParameters(category, numberLists);
        //then
    }

    @Test
    void validateInputParametersWithSingleNumberShouldPass() {
        //given
        String category = "Animals";
        String numberLists = "2";
        //when
        validationService.validateInputParameters(category, numberLists);
        //then
    }

    @Test
    void validateInputParametersThrowExceptionWithStringAsNumber() {
        //given
        String category = "Animals";
        String numberLists = "1,2,a";
        //when
        assertThrows(IllegalArgumentException.class, () -> validationService.validateInputParameters(category, numberLists));
        //then
    }

    @Test
    void validateInputParametersThrowExceptionWithBlankListNumbers() {
        //given
        String category = "Animals";
        String numberLists = "";
        //when
        assertThrows(IllegalArgumentException.class, () -> validationService.validateInputParameters(category, numberLists));
        //then
    }

    @Test
    void validateInputParametersThrowExceptionWithBlankNullNumbers() {
        //given
        String category = "Animals";
        String numberLists = "";
        //when
        assertThrows(IllegalArgumentException.class, () -> validationService.validateInputParameters(category, numberLists));
        //then
    }

    @Test
    void validateInputParametersThrowExceptionWithNumberGreaterThan20() {
        //given
        String category = "Animals";
        String numberLists = "22";
        //when
        assertThrows(IllegalArgumentException.class, () -> validationService.validateInputParameters(category, numberLists));
        //then
    }

    @Test
    void validateInputParametersThrowExceptionWithNumberLowerThan1() {
        //given
        String category = "Animals";
        String numberLists = "0";
        //when
        assertThrows(IllegalArgumentException.class, () -> validationService.validateInputParameters(category, numberLists));
        //then
    }

    @Test
    void validateInputParametersShouldThrowExceptionWithBlankCategory() {
        //given
        String category = "";
        String numberLists = "1,2,3";
        //when
        assertThrows(IllegalArgumentException.class, () -> validationService.validateInputParameters(category, numberLists));
        //then
    }

    @Test
    void validateInputParametersShouldThrowExceptionWithNullCategory() {
        //given
        String category = null;
        String numberLists = "1,2,3";
        //when
        assertThrows(IllegalArgumentException.class, () -> validationService.validateInputParameters(category, numberLists));
        //then
    }

    @Test
    void validateInputFileShouldPass() {
        //given
        List<Record> records = prepareInputFileToValidate("test-mapping.csv");
        //when
        validationService.validateInputCsvFile(records);
        //then
    }

    @Test
    void validateInputFileShouldFailWithLessCategories() {
        //given
        List<Record> records = prepareInputFileToValidate("incorrect-mapping-file-less-cat.csv");
        //when
        assertThrows(ValidatingFileException.class, () -> validationService.validateInputCsvFile(records));
        //then
    }

    @Test
    void validateInputFileShouldFailWithMoreCategories() {
        //given
        List<Record> records = prepareInputFileToValidate("incorrect-mapping-file-more-cat.csv");
        //when
        assertThrows(ValidatingFileException.class, () -> validationService.validateInputCsvFile(records));
        //then
    }

    @Test
    void validateInputFileShouldFailWithLessRecordsThan20() {
        //given
        List<Record> records = prepareInputFileToValidate("incorrect-mapping-file-less-records.csv");
        //when
        assertThrows(ValidatingFileException.class, () -> validationService.validateInputCsvFile(records));
        //then
    }

    @Test
    void validateInputFileShouldFailWithMoreRecordsThan20() {
        //given
        List<Record> records = prepareInputFileToValidate("incorrect-mapping-file-more-records.csv");
        //when
        assertThrows(ValidatingFileException.class, () -> validationService.validateInputCsvFile(records));
        //then
    }

    private List<Record> prepareInputFileToValidate(String fileName) {
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);
        CsvParser parser = new CsvParser(new CsvParserSettings());
        return parser.parseAllRecords(is);
    }

}