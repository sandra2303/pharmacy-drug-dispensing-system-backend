package com.pharmacy.dispensing.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pharmacy.dispensing.domain.Counter;

public interface CounterRepository extends JpaRepository<Counter, UUID> {

    // C2 - JPQL: NOT EXISTS subquery
    @Query("SELECT c FROM Counter c "
         + "WHERE NOT EXISTS (SELECT p FROM Pharmacist p WHERE p.counter = c) "
         + "ORDER BY c.counterNumber ASC")
    List<Counter> findFreeCounters();

    // D4 - DERIVED: findBy
    Optional<Counter> findByCounterNumber(int counterNumber);
}