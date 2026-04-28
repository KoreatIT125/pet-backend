package com.disaster.safety.petmediscan.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.disaster.safety.petmediscan.dto.DiseaseResponse;
import com.disaster.safety.petmediscan.entity.Diagnosis;
import com.disaster.safety.petmediscan.entity.Pet;
import com.disaster.safety.petmediscan.entity.Types;
import com.disaster.safety.petmediscan.repository.DiagnosisRepository;
import com.disaster.safety.petmediscan.service.DiagnosisService;
import com.disaster.safety.petmediscan.service.PetService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/diagnosis")
public class DiagnosisController {
    private final DiagnosisService diagnosisService;
    private final PetService petService;
    private final DiagnosisRepository diagnosisRepository;

    @PostMapping("/skin")
    public String setSkin(Integer petId, String image){
        Pet pet = petService.get(petId);
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPet(pet);
        diagnosis.setType(Types.Skin);
        diagnosis.setResult("custom");
        diagnosis.setImage_url(image);
        diagnosis.setConfidence(null);
        diagnosis.setDiagnosis_date(LocalDateTime.now());
        return "";
    }

    @PostMapping("/eye")
    public String setEye(Integer petId, String image){
        Pet pet = petService.get(petId);
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPet(pet);
        diagnosis.setType(Types.Eye);
        diagnosis.setResult("custom");
        diagnosis.setImage_url(image);
        diagnosis.setConfidence(null);
        diagnosis.setDiagnosis_date(LocalDateTime.now());
        return "";
    }

    @PostMapping
    public ResponseEntity<List<DiseaseResponse>> diagnose(
            @RequestPart("image") MultipartFile file,
            @RequestPart("type") Types type) {
        try {
            return ResponseEntity.ok(diagnosisService.diagnose(file, type));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/history/{petId}")
    public String detail(@PathVariable("petId") Integer petId){
        Pet pet = petService.get(petId);
        diagnosisRepository.findByPet(pet);
        
        return "diagnosis";
    }
}
