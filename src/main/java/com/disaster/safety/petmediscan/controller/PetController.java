package com.disaster.safety.petmediscan.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.disaster.safety.petmediscan.dto.PetResponseDto;
import com.disaster.safety.petmediscan.form.PetCreateForm;
import com.disaster.safety.petmediscan.service.PetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;
    
    // memberId(파라미터)로 Pet List 획득하는 Get 매핑
    @GetMapping("")
    public ResponseEntity<List<PetResponseDto>> petList(@RequestParam("memberId") Long memberId) {
        List<PetResponseDto> pets = petService.findAllByMember(memberId)
                .stream()
                .map(PetResponseDto::from)
                .toList();
        return ResponseEntity.ok(pets);
    }

    // petCreateForm(리퀘스트바디)으로 Pet 개체 생성하는 Post 매핑
    @PostMapping("")
    public ResponseEntity<PetResponseDto> createPet(@Valid @RequestBody PetCreateForm petCreateForm) {
        String breed = petCreateForm.getBreed() == null ? "" : petCreateForm.getBreed();
        var pet = petService.create(
                petCreateForm.getName(),
                petCreateForm.getSpecies(),
                petCreateForm.getMemberId(),
                breed,
                petCreateForm.getBirth_date());

        return ResponseEntity.status(HttpStatus.CREATED).body(PetResponseDto.from(pet));
    }

    // petId(경로)로 Pet 단일 개체 획득하는 Get 매핑
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDto> getPet(@PathVariable("petId") Long petId) {
        return ResponseEntity.ok(PetResponseDto.from(petService.get(petId)));
    }

    // petId(경로)와 petCreateForm(리퀘스트바디)로 Pet 단일 개체 수정하는 Put 매핑
    @PutMapping("/{petId}")
    public ResponseEntity<PetResponseDto> modifyPet(@RequestBody PetCreateForm petCreateForm,
            @PathVariable("petId") Long petId) {
        var pet = petService.modify(petId, petCreateForm.getBreed(), petCreateForm.getBirth_date());
        return ResponseEntity.ok(PetResponseDto.from(pet));
    }
}
