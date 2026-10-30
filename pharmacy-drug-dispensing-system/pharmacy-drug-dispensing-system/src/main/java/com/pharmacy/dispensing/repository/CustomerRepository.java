package com.pharmacy.dispensing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmacy.dispensing.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // B1 - DERIVED: IgnoreCase + OrderBy
    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // C4 - JPQL: DISTINCT + path navigation (o.customer, o.pharmacist.id)
    @Query("SELECT DISTINCT o.customer FROM DrugOrder o WHERE o.pharmacist.id = :pharmacistId")
    List<Customer> findCustomersOfPharmacist(@Param("pharmacistId") UUID pharmacistId);
}