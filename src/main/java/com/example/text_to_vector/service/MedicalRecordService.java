package com.example.text_to_vector.service;

import java.util.List;

public interface MedicalRecordService {
    void saveToVectorSpace(String patientName, String historyText);
    List<String> findSimilar(String query);
}