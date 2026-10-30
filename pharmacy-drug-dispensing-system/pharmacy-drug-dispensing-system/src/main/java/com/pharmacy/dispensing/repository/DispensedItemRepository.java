package com.pharmacy.dispensing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmacy.dispensing.domain.DispensedItem;

public interface DispensedItemRepository extends JpaRepository<DispensedItem, UUID> {

    // E2 - JPQL: path navigation d.drugOrder.customer.customerNumber + ORDER BY
    @Query("SELECT d FROM DispensedItem d "
         + "WHERE d.drugOrder.customer.customerNumber = :customerNumber "
         + "ORDER BY d.dispensedDate DESC")
    List<DispensedItem> findByCustomerNumber(@Param("customerNumber") String customerNumber);

    // E3 - DERIVED: existsBy (drugOrder.id)
    boolean existsByDrugOrderId(UUID drugOrderId);
}