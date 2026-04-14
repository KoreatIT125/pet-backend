package com.petmediscan.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.disaster.safety.user.SiteUser;
import com.petmediscan.entity.Pet;
import com.petmediscan.entity.Species;
import com.petmediscan.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    public Pet create(String name, Species species, SiteUser user, String breed, LocalDateTime birthday){
        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setBirth_date(birthday);
        pet.setUser(user);

        petRepository.save(pet);
        return pet;
    }
    public List<Pet> findAllByUser(SiteUser user){
        return petRepository.findByUser(user);
    }
    public Pet get(long id){
        return petRepository.findById(id).get();
    }
    public void modify(Pet pet, String breed, LocalDateTime birth) {
        pet.setBreed(breed);
        pet.setBirth_date(birth);
        
        petRepository.save(pet);
    }
}
