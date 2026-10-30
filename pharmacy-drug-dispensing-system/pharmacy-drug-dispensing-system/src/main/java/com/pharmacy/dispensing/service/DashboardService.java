package com.pharmacy.dispensing.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dispensing.domain.DrugOrderStatus;
import com.pharmacy.dispensing.repository.CounterRepository;
import com.pharmacy.dispensing.repository.CustomerRepository;
import com.pharmacy.dispensing.repository.DispensedItemRepository;
import com.pharmacy.dispensing.repository.DrugOrderRepository;
import com.pharmacy.dispensing.repository.PharmacistRepository;

@Service
public class DashboardService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PharmacistRepository pharmacistRepo;

    @Autowired
    private CounterRepository counterRepo;

    @Autowired
    private DrugOrderRepository drugOrderRepo;

    @Autowired
    private DispensedItemRepository dispensedItemRepo;

    // LinkedHashMap keeps the insertion order in the JSON
    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("customers", customerRepo.count());
        summary.put("pharmacists", pharmacistRepo.count());
        summary.put("freeCounters", (long) counterRepo.findFreeCounters().size());   // reuse C2
        summary.put("ordersToday", drugOrderRepo.countByOrderDate(LocalDate.now()));
        summary.put("received", drugOrderRepo.countByStatusEquals(DrugOrderStatus.RECEIVED));
        summary.put("dispensedItems", dispensedItemRepo.count());
        return summary;
    }
}