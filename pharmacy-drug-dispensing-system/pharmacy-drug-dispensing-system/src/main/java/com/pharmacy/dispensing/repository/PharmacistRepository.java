package com.pharmacy.dispensing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmacy.dispensing.domain.Pharmacist;

public interface PharmacistRepository extends JpaRepository<Pharmacist, UUID> {

    // C1 - JPQL: JOIN on the collection + LOWER (DISTINCT: a pharmacist with two matching classes appears once)
    @Query("SELECT DISTINCT p FROM Pharmacist p JOIN p.drugClasses c "
         + "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Pharmacist> findByDrugClassName(@Param("name") String name);

    // D4 - DERIVED: existsBy + And + Not
    // "is this counter used by a pharmacist other than this one?"
    boolean existsByCounterIdAndIdNot(UUID counterId, UUID pharmacistId);
}