package com.example.text_to_vector.service;

import org.springframework.web.multipart.MultipartFile;

public interface DatasetIngestService {
    String ingestCsv(MultipartFile file, String dataset, String sourceName);
}