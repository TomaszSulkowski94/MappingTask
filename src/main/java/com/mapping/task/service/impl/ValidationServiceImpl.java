package com.mapping.task.service.impl;

import com.mapping.task.service.ValidationService;
import com.univocity.parsers.common.record.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import utils.ValidatingFileException;

import java.util.List;

import static io.micrometer.common.util.StringUtils.isBlank;

@Service
@Slf4j
public class ValidationServiceImpl implements ValidationService {

    @Override
    public void validateInputParameters(String category, String numberLists) {
        validateCategory(category);
        validateNumbers(numberLists);
    }

    @Override
    public void validateInputCsvFile(List<Record> records) {
        log.info("Validating input file");
        if (records.size() != 21) { //20 mapping rows + 1 header row
            throw new ValidatingFileException("Input file contains {%s} rows. It should have 20 rows.".formatted(records.size() - 1));
        }
        if (records.getFirst().getValues().length != 11) { //10 mapping categories + 1 ordinal number
            throw new ValidatingFileException("Input file contains {%s} mapping columns. It should have 10 categories.".formatted(records.getFirst().getValues().length - 1));
        }
        log.info("Input file validated successfully");
    }

    private void validateCategory(String category) {
        log.info("Validating category {}", category);
        if (isBlank(category)) {
            throw new IllegalArgumentException("Provided category is blank!");
        }
        log.info("Category validated successfully");
    }

    private void validateNumbers(String numberLists) {
        log.info("Validating number of lists {}", numberLists);
        if (isBlank(numberLists)) {
            throw new IllegalArgumentException("Provided numberLists is blank!");
        }
        String[] split = numberLists.split(",");
        for (String valueToParse : split) {
            try {
                validateNumber(Integer.parseInt(valueToParse));
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Provided value {%s} can't be parsed to number".formatted(valueToParse));
            }
        }
    }

    private void validateNumber(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("Provided {%s} number can't be lower than 1. Accepted range is 1-20".formatted(i));
        }
        if (i > 20) {
            throw new IllegalArgumentException("Provided {%s} number can't be greater than 20. Accepted range is 1-20".formatted(i));
        }
    }
}
