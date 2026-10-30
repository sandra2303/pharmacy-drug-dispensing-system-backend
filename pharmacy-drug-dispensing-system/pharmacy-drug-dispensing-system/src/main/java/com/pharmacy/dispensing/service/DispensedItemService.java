package com.pharmacy.dispensing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dispensing.domain.DispensedItem;
import com.pharmacy.dispensing.domain.DrugOrder;
import com.pharmacy.dispensing.domain.DrugOrderStatus;
import com.pharmacy.dispensing.repository.DispensedItemRepository;
import com.pharmacy.dispensing.repository.DrugOrderRepository;

@Service
public class DispensedItemService {

    @Autowired
    private DispensedItemRepository dispensedItemRepo;

    @Autowired
    private DrugOrderRepository drugOrderRepo;

    // E1: write a dispensed item (only for DISPENSED drug orders)
    public String saveDispensedItem(UUID drugOrderId, DispensedItem item) {
        Optional<DrugOrder> orderOpt = drugOrderRepo.findById(drugOrderId);
        if (orderOpt.isEmpty()) {
            return "The drug order with that id does not exist";
        }
        DrugOrder drugOrder = orderOpt.get();
        if (drugOrder.getStatus() != DrugOrderStatus.DISPENSED) {
            return "Dispensed items are only for DISPENSED drug orders";
        }
        item.setDispensedDate(LocalDate.now());   // the service sets the date ...
        item.setDrugOrder(drugOrder);             // ... and links the drug order
        dispensedItemRepo.save(item);
        return "Dispensed item saved successfully";
    }

    public List<DispensedItem> getAllDispensedItems() {
        return dispensedItemRepo.findAll();
    }

    // E2
    public List<DispensedItem> findByCustomerNumber(String customerNumber) {
        return dispensedItemRepo.findByCustomerNumber(customerNumber);
    }

    // E3
    public String deleteDispensedItem(UUID id) {
        if (!dispensedItemRepo.existsById(id)) {
            return "The dispensed item with that id does not exist";
        }
        dispensedItemRepo.deleteById(id);
        return "Dispensed item deleted successfully";
    }
}