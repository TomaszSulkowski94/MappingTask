package com.mapping.task.service.impl;

import com.mapping.task.service.FileLoaderService;
import com.mapping.task.service.MappingService;
import com.mapping.task.service.ValidationService;
import com.univocity.parsers.common.record.Record;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MappingServiceImpl implements MappingService {

    private final FileLoaderService fileLoaderService;
    private final ValidationService validationService;

    @Override
    public Map<Integer, List<String>> mapValues(String category, String numberLists) {
        validationService.validateInputParameters(category, numberLists);

        List<Integer> numbers = parseNumbersToList(numberLists);
        List<Record> records = fileLoaderService.loadMappingFile();

        return mapValuesToResults(numbers, records, category);
    }

    private Map<Integer, List<String>> mapValuesToResults(List<Integer> numbers, List<Record> records, String category) {
        Map<Integer, List<String>> results = new LinkedHashMap<>();
        for (Integer number : numbers) {
            List<Integer> divisiors = findDivisiors(number);
            List<String> mappedValues = divisiors.stream().map(div -> mapToValue(category, div, records)).toList();
            results.put(number, mappedValues);
        }
        return results;
    }

    private String mapToValue(String category, Integer divisior, List<Record> records) {
        try {
            return records.get(divisior).getString(category);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    private List<Integer> findDivisiors(Integer number) {
        List<Integer> divisiors = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            if (i > number) {
                break;
            }
            if (number % i == 0) {
                divisiors.add(i);
            }
        }
        return divisiors;
    }

    private List<Integer> parseNumbersToList(String numberLists) {
        return Arrays.stream(numberLists.split(",")).map(Integer::parseInt).toList();
    }
}
