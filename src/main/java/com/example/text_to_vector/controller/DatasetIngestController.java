package com.example.text_to_vector.controller;

import com.example.text_to_vector.service.DatasetIngestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class DatasetIngestController {

    private final DatasetIngestService datasetIngestService;

    public DatasetIngestController(DatasetIngestService datasetIngestService) {
        this.datasetIngestService = datasetIngestService;
    }

    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String ingest(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "dataset", required = false, defaultValue = "MedSynth") String dataset,
            @RequestParam(value = "sourceName", required = false) String sourceName
    ) {
        String actualSourceName = (sourceName == null || sourceName.isBlank())
                ? file.getOriginalFilename()
                : sourceName;

        return datasetIngestService.ingestCsv(file, dataset, actualSourceName);
    }
}