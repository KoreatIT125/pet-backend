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

    @GetMapping("")
    public ResponseEntity<List<PetResponseDto>> petList(@RequestParam("memberId") Long memberId) {
        List<PetResponseDto> pets = petService.findAllByMember(memberId)
                .stream()
                .map(PetResponseDto::from)
                .toList();
        return ResponseEntity.ok(pets);
    }

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

    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDto> getPet(@PathVariable("petId") Long petId) {
        return ResponseEntity.ok(PetResponseDto.from(petService.get(petId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDto> modifyPet(@RequestBody PetCreateForm petCreateForm,
            @PathVariable("id") Long id) {
        var pet = petService.modify(id, petCreateForm.getBreed(), petCreateForm.getBirth_date());
        return ResponseEntity.ok(PetResponseDto.from(pet));
    }
}
