package com.petmediscan.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.petmediscan.entity.Diagnosis;
import com.petmediscan.entity.Types;
import com.petmediscan.entity.Pet;
import com.petmediscan.repository.DiagnosisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    
    public Diagnosis create(Pet pet, Types type,String imageUrl, String result, BigDecimal confidence){
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPet(pet);
        diagnosis.setType(type);
        diagnosis.setImage_url(imageUrl);
        diagnosisRepository.save(diagnosis);
        diagnosis.setResult(result);
        diagnosis.setConfidence(confidence);
        diagnosis.setDiagnosis_date(LocalDateTime.now());
        
        diagnosisRepository.save(diagnosis);
        return diagnosis;
    }

    public List<Diagnosis> findAllByPet(Pet pet){
        List<Diagnosis> diagnosis = diagnosisRepository.findByPet(pet);
        return diagnosis;
    }
}
