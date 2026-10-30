package com.pharmacy.dispensing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pharmacy.dispensing.domain.DrugClass;

public interface DrugClassRepository extends JpaRepository<DrugClass, UUID> {

    // C3 - JPQL: IS EMPTY on the pharmacists collection (no COUNT)
    @Query("SELECT d FROM DrugClass d WHERE d.pharmacists IS EMPTY ORDER BY d.name ASC")
    List<DrugClass> findUnusedDrugClasses();
}