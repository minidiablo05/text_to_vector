package com.example.text_to_vector.service.impl;

import com.example.text_to_vector.service.MedicalRecordService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final VectorStore vectorStore;

    public MedicalRecordServiceImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void saveToVectorSpace(String patientName, String historyText) {
        try {
            Document document = new Document(
                    historyText,
                    Map.of("patientName", patientName)
            );

            vectorStore.add(List.of(document));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении записи в векторное хранилище", e);
        }
    }

    @Override
    public List<String> findSimilar(String query) {
        try {
            SearchRequest request = SearchRequest.builder()
                    .query(query)
                    .topK(5)
                    .similarityThreshold(0.5)
                    .build();

            List<Document> results = vectorStore.similaritySearch(request);

            return results.stream()
                    .map(doc -> {
                        String patientName = String.valueOf(
                                doc.getMetadata().getOrDefault("patientName", "Unknown Patient")
                        );
                        String text = doc.getText() == null ? "" : doc.getText();

                        return "Пациент: " + patientName + "\nИстория: " + text;
                    })
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске похожих записей", e);
        }
    }
}