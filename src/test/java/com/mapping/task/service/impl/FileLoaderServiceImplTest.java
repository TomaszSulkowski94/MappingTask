package com.mapping.task.service.impl;

import com.mapping.task.service.ValidationService;
import com.univocity.parsers.common.record.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import utils.ValidatingFileException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class FileLoaderServiceImplTest {

    @Mock
    private ValidationService validationService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void loadMappingFileShouldPass() {
        //given
        FileLoaderServiceImpl fileLoaderService = new FileLoaderServiceImpl(validationService);
        doNothing().when(validationService).validateInputCsvFile(anyList());
        ReflectionTestUtils.setField(fileLoaderService, "mappingFile", "test-mapping.csv");
        //when
        List<Record> records = fileLoaderService.loadMappingFile();
        //then
        assertEquals(21, records.size());
    }

    @Test
    void loadMappingFileShouldFailOnValidation() {
        //given
        FileLoaderServiceImpl fileLoaderService = new FileLoaderServiceImpl(validationService);
        doThrow(ValidatingFileException.class).when(validationService).validateInputCsvFile(anyList());
        ReflectionTestUtils.setField(fileLoaderService, "mappingFile", "test-mapping.csv");
        //when
        assertThrows(ValidatingFileException.class, fileLoaderService::loadMappingFile);
        //then
    }
}