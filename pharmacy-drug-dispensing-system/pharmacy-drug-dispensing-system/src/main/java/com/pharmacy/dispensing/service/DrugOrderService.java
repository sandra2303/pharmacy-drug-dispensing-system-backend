package com.pharmacy.dispensing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dispensing.domain.DrugOrder;
import com.pharmacy.dispensing.domain.DrugOrderStatus;
import com.pharmacy.dispensing.repository.DispensedItemRepository;
import com.pharmacy.dispensing.repository.DrugOrderRepository;

@Service
public class DrugOrderService {

    @Autowired
    private DrugOrderRepository drugOrderRepo;

    @Autowired
    private DispensedItemRepository dispensedItemRepo;

    // D1: safe saving
    public String saveDrugOrder(DrugOrder drugOrder) {
        if (drugOrder.getOrderDate().isBefore(LocalDate.now())) {
            return "Drug order date cannot be in the past";
        }
        boolean alreadyHasOrder = drugOrderRepo.existsByPharmacistIdAndOrderDateAndStatusNot(
                drugOrder.getPharmacist().getId(),
                drugOrder.getOrderDate(),
                DrugOrderStatus.REJECTED);
        if (alreadyHasOrder) {
            return "Pharmacist already has a drug order on that date";
        }
        // a new drug order always starts as RECEIVED, whatever the JSON says
        drugOrder.setStatus(DrugOrderStatus.RECEIVED);
        drugOrderRepo.save(drugOrder);
        return "Drug order saved successfully";
    }

    public List<DrugOrder> getAllDrugOrders() {
        return drugOrderRepo.findAll();
    }

    public Optional<DrugOrder> getDrugOrderById(UUID id) {
        return drugOrderRepo.findById(id);
    }

    // D2: the status is NOT changed here any more
    public String updateDrugOrder(UUID id, DrugOrder updated) {
        Optional<DrugOrder> existing = drugOrderRepo.findById(id);
        if (existing.isEmpty()) {
            return "The drug order with that id does not exist";
        }
        DrugOrder drugOrder = existing.get();
        drugOrder.setOrderDate(updated.getOrderDate());
        drugOrder.setPrescribingDoctor(updated.getPrescribingDoctor());
        drugOrder.setCustomer(updated.getCustomer());
        drugOrder.setPharmacist(updated.getPharmacist());
        drugOrderRepo.save(drugOrder);
        return "Drug order updated successfully";
    }

    public String deleteDrugOrder(UUID id) {
        if (!drugOrderRepo.existsById(id)) {
            return "The drug order with that id does not exist";
        }
        // E3: a drug order that has dispensed items cannot be deleted
        if (dispensedItemRepo.existsByDrugOrderId(id)) {
            return "That drug order has dispensed items, delete them first";
        }
        drugOrderRepo.deleteById(id);
        return "Drug order deleted successfully";
    }

    // B2
    public List<DrugOrder> findByStatus(DrugOrderStatus status) {
        return drugOrderRepo.findByStatusOrderByOrderDateAsc(status);
    }

    // B3
    public List<DrugOrder> findBetween(LocalDate start, LocalDate end) {
        return drugOrderRepo.findByOrderDateBetweenOrderByOrderDateAsc(start, end);
    }

    // D2: status flow  RECEIVED -> PREPARING|REJECTED, PREPARING -> DISPENSED|REJECTED
    public String changeStatus(UUID id, DrugOrderStatus newStatus) {
        Optional<DrugOrder> opt = drugOrderRepo.findById(id);
        if (opt.isEmpty()) {
            return "The drug order with that id does not exist";
        }
        DrugOrder drugOrder = opt.get();
        DrugOrderStatus current = drugOrder.getStatus();

        boolean allowed =
                (current == DrugOrderStatus.RECEIVED
                    && (newStatus == DrugOrderStatus.PREPARING
                        || newStatus == DrugOrderStatus.REJECTED))
             || (current == DrugOrderStatus.PREPARING
                    && (newStatus == DrugOrderStatus.DISPENSED
                        || newStatus == DrugOrderStatus.REJECTED));

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        drugOrder.setStatus(newStatus);
        drugOrderRepo.save(drugOrder);
        return "Drug order status updated successfully";
    }

    // F1
    public List<Object[]> countByStatus() {
        return drugOrderRepo.countByStatus();
    }

    // F2: @Transactional is required for UPDATE / DELETE queries
    @Transactional
    public String cancelDay(UUID pharmacistId, LocalDate date) {
        int count = drugOrderRepo.cancelDay(pharmacistId, date,
                DrugOrderStatus.REJECTED, DrugOrderStatus.DISPENSED);
        return count + " drug orders cancelled";
    }

    // Bonus: paging
    public Page<DrugOrder> getPage(Pageable pageable) {
        return drugOrderRepo.findAll(pageable);
    }
}