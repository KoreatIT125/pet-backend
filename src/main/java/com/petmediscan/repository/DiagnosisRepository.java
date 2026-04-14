package com.petmediscan.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petmediscan.entity.Diagnosis;
import com.petmediscan.entity.Pet;

public interface DiagnosisRepository extends JpaRepository<Diagnosis,Long> {
    List<Diagnosis> findByPet(Pet pet);
}
