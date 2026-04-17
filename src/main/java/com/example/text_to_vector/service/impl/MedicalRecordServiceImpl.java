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
    public void saveToVectorSpace(String patientId, String historyText) {
        Document document = new Document(
                historyText,
                Map.of("patientId", patientId)
        );

        vectorStore.add(List.of(document));
    }

    @Override
    public List<String> findSimilar(String query) {
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(5)
                .similarityThreshold(0.5)
                .build();

        List<Document> results = vectorStore.similaritySearch(request);

        return results.stream()
                .map(doc -> {
                    String patientID = String.valueOf(
                            doc.getMetadata().getOrDefault("patientId", "unknown")
                    );
                    String text = doc.getText() == null ? "" : doc.getText();

                    return "IDX: " + patientID + "\nNote: " + text;
                })
                .toList();
    }
}