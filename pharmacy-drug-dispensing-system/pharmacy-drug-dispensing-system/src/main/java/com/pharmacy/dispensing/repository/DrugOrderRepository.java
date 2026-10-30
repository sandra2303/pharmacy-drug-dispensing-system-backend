package com.pharmacy.dispensing.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmacy.dispensing.domain.DrugOrder;
import com.pharmacy.dispensing.domain.DrugOrderStatus;

public interface DrugOrderRepository extends JpaRepository<DrugOrder, UUID> {

    // B2 - DERIVED: enum parameter + OrderBy
    List<DrugOrder> findByStatusOrderByOrderDateAsc(DrugOrderStatus status);

    // B3 - DERIVED: Between (both ends inclusive) + OrderBy
    List<DrugOrder> findByOrderDateBetweenOrderByOrderDateAsc(LocalDate start, LocalDate end);

    // D1 - DERIVED: existsBy + And + Not   (PharmacistId = pharmacist.id)
    boolean existsByPharmacistIdAndOrderDateAndStatusNot(UUID pharmacistId, LocalDate date, DrugOrderStatus status);

    // F1 - JPQL: GROUP BY + COUNT (a status with zero rows does not appear)
    @Query("SELECT o.status, COUNT(o) FROM DrugOrder o GROUP BY o.status")
    List<Object[]> countByStatus();

    // F2 - JPQL: one UPDATE query, no Java loop
    @Modifying(clearAutomatically = true)
    @Query("UPDATE DrugOrder o SET o.status = :newStatus "
         + "WHERE o.pharmacist.id = :pharmacistId "
         + "AND o.orderDate = :date "
         + "AND o.status <> :keepStatus")
    int cancelDay(@Param("pharmacistId") UUID pharmacistId,
                  @Param("date") LocalDate date,
                  @Param("newStatus") DrugOrderStatus newStatus,
                  @Param("keepStatus") DrugOrderStatus keepStatus);

    // Bonus - DERIVED countBy (returns long)
    long countByOrderDate(LocalDate date);

    // Bonus - DERIVED countBy (named ...Equals so it does not clash with countByStatus() above)
    long countByStatusEquals(DrugOrderStatus status);
}