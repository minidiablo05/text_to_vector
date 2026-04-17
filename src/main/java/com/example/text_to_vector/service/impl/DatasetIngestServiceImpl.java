package com.example.text_to_vector.service.impl;

import com.example.text_to_vector.service.DatasetIngestService;
import com.example.text_to_vector.service.MedicalRecordService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class DatasetIngestServiceImpl implements DatasetIngestService {

    private final MedicalRecordService medicalRecordService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DatasetIngestServiceImpl(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @Override
    public String ingestCsv(MultipartFile file, String dataset, String sourceName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой или не передан");
        }

        int imported = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                JsonNode node = objectMapper.readTree(line);

                String idx = node.path("idx").asText();
                String note = node.path("note").asText();

                if (idx.isBlank() || note.isBlank()) {
                    continue;
                }

                medicalRecordService.saveToVectorSpace(idx, note);
                imported++;

                if (imported % 10 == 0) {
                    System.out.println("Уже загружено записей: " + imported);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обработки JSONL: " + e.getMessage(), e);
        }

        return "Импорт завершен. Загружено записей: " + imported;
    }
}