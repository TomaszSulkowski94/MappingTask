package com.mapping.task.service;

import com.univocity.parsers.common.record.Record;

import java.util.List;

public interface FileLoaderService {


    List<Record> loadMappingFile();

}
