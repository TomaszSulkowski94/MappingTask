package com.mapping.task.service.impl;

import com.mapping.task.service.FileLoaderService;
import com.mapping.task.service.ValidationService;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class MappingServiceImplTest {

    @Mock
    private FileLoaderService fileLoaderService;
    @Mock
    private ValidationService validationService;

    private MappingServiceImpl mappingService;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);
        mappingService = new MappingServiceImpl(fileLoaderService, validationService);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    static Stream<Arguments> mapValuesDataWithSingleNumber() {
        return Stream.of(
                Arguments.of("Animals", "1", List.of("Lion")),
                Arguments.of("Car", "2", List.of("Mazda", "BMW")),
                Arguments.of("Country", "6", List.of("Poland", "Germany", "France", "Portugal")),
                Arguments.of("Color", "10", List.of("Red", "Blue", "Black", "Orange")),
                Arguments.of("Job", "13", List.of("Doctor", "Lawyer")),
                Arguments.of("Sport", "15", List.of("Football", "Tennis", "Running", "Climbing")),
                Arguments.of("City", "17", List.of("Warsaw", "Budapest")),
                Arguments.of("Technology", "20", List.of("AI", "Cloud", "IoT", "Big Data", "Cybersecurity", "Smart Cities"))
        );
    }

    @ParameterizedTest
    @MethodSource("mapValuesDataWithSingleNumber")
    void mapValuesShouldPassWithSingleNumberOnList(String category, String number, List<String> mappedValues) {
        //given
        //when
        doNothing().when(validationService).validateInputParameters(anyString(), anyString());
        when(fileLoaderService.loadMappingFile()).thenReturn(prepareRecordList());
        Map<Integer, List<String>> integerListMap = mappingService.mapValues(category, number);
        //then
        assertEquals(1, integerListMap.size());
        assertEquals(mappedValues, integerListMap.get(Integer.parseInt(number)));
    }

    static Stream<Arguments> mapValuesWithMultipleNumbers() {
        return Stream.of(
                Arguments.of("Animals", "1", 1, Map.of(1, List.of("Lion"))),
                Arguments.of("Car", "2,3", 2, Map.of(2, List.of("Mazda", "BMW"), 3, List.of("Mazda", "Audi"))),
                Arguments.of("Country", "4,5,20", 3, Map.of(4, List.of("Poland", "Germany", "Italy"), 5, List.of("Poland", "Spain"),20,List.of("Poland", "Germany", "Italy", "Spain", "Denmark", "Canada")))
        );
    }

    @ParameterizedTest
    @MethodSource("mapValuesWithMultipleNumbers")
    void mapValuesWithMultipleNumbersShouldPass(String category, String numbersList, int expectedSize, Map<Integer, List<String>> expectedResults) {
        //given
        //when
        doNothing().when(validationService).validateInputParameters(anyString(), anyString());
        when(fileLoaderService.loadMappingFile()).thenReturn(prepareRecordList());
        Map<Integer, List<String>> resultMap = mappingService.mapValues(category, numbersList);
        //then
        assertEquals(expectedSize, resultMap.size());
        Set<Integer> keys = expectedResults.keySet();
        for (Integer key : keys) {
            assertEquals(expectedResults.get(key), resultMap.get(key));
        }
    }

    @Test
    void mapValuesShouldFailWithIncorrectCategory() {
        //given
        String category = "IncorrectMappingCategory";
        String numberList = "1,2,3";
        //when
        doNothing().when(validationService).validateInputParameters(anyString(), anyString());
        when(fileLoaderService.loadMappingFile()).thenReturn(prepareRecordList());
        //then
        assertThrows(IllegalArgumentException.class, () -> mappingService.mapValues(category, numberList));
    }


    private List<Record> prepareRecordList() {
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("test-mapping.csv");
        CsvParser parser = new CsvParser(new CsvParserSettings());
        return parser.parseAllRecords(is);
    }
}