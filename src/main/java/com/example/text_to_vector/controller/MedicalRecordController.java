package com.example.text_to_vector.controller;

import com.example.text_to_vector.dto.MedicalRecordDTO;
import com.example.text_to_vector.service.MedicalRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class MedicalRecordController {

    private final MedicalRecordService service;

    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public String saveRecord(@RequestBody MedicalRecordDTO dto) {
        service.saveToVectorSpace(dto.patientName(), dto.historyText());
        return "История болезни пациента " + dto.patientName() + " успешно векторизована и сохранена!";
    }

    @GetMapping("/search")
    public List<String> searchSimilar(@RequestParam String query) {
        return service.findSimilar(query);
    }
}

