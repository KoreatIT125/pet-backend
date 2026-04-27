package com.disaster.safety.petmediscan.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.disaster.safety.member.entity.Member;
import com.disaster.safety.member.repository.MemberRepository;
import com.disaster.safety.petmediscan.entity.Pet;
import com.disaster.safety.petmediscan.entity.Species;
import com.disaster.safety.petmediscan.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    public Pet create(String name, Species species, Long memberId, String breed, LocalDateTime birthday) {
        Member member = memberRepository.findMember(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + memberId);
        }

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setBirth_date(birthday);
        pet.setMember(member);

        return petRepository.save(pet);
    }

    public List<Pet> findAllByMember(Long memberId) {
        Member member = memberRepository.findMember(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + memberId);
        }

        return petRepository.findByMember(member);
    }

    public Pet get(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다. id=" + id));
    }

    public Pet modify(long id, String breed, LocalDateTime birth) {
        Pet pet = get(id);
        pet.setBreed(breed);
        pet.setBirth_date(birth);

        return petRepository.save(pet);
    }
}
