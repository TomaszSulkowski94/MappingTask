package com.mapping.task.service;

import java.util.List;
import java.util.Map;

public interface MappingService {

    Map<Integer, List<String>> mapValues(String category, String mapping);
}
