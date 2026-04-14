package com.petmediscan.repository;

import com.disaster.safety.user.SiteUser;
import com.petmediscan.entity.Pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Long>{
    List<Pet> findByUser(SiteUser user);
}
