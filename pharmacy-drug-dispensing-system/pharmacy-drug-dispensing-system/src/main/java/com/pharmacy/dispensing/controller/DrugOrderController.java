package com.pharmacy.dispensing.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dispensing.domain.DrugOrder;
import com.pharmacy.dispensing.domain.DrugOrderStatus;
import com.pharmacy.dispensing.service.DrugOrderService;

@RestController
@RequestMapping(value = "/api/drug-orders")
public class DrugOrderController {

    @Autowired
    private DrugOrderService drugOrderService;

    // A2 + D1
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDrugOrder(@RequestBody DrugOrder drugOrder) {
        String msg = drugOrderService.saveDrugOrder(drugOrder);
        if (msg.equals("Drug order saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else if (msg.equals("Drug order date cannot be in the past")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DrugOrder>> getAllDrugOrders() {
        return new ResponseEntity<>(drugOrderService.getAllDrugOrders(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDrugOrderById(@PathVariable UUID id) {
        Optional<DrugOrder> drugOrderOpt = drugOrderService.getDrugOrderById(id);
        if (drugOrderOpt.isPresent()) {
            return new ResponseEntity<>(drugOrderOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The drug order with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateDrugOrder(@PathVariable UUID id, @RequestBody DrugOrder drugOrder) {
        String msg = drugOrderService.updateDrugOrder(id, drugOrder);
        if (msg.equals("Drug order updated successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    // A2 + E3
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDrugOrder(@PathVariable UUID id) {
        String msg = drugOrderService.deleteDrugOrder(id);
        if (msg.equals("Drug order deleted successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } else if (msg.equals("That drug order has dispensed items, delete them first")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    // B2
    @GetMapping(value = "/by-status")
    public ResponseEntity<List<DrugOrder>> byStatus(@RequestParam DrugOrderStatus status) {
        return new ResponseEntity<>(drugOrderService.findByStatus(status), HttpStatus.OK);
    }

    // B3: the Strings are parsed into LocalDate here in the controller
    @GetMapping(value = "/between")
    public ResponseEntity<List<DrugOrder>> between(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(drugOrderService.findBetween(startDate, endDate), HttpStatus.OK);
    }

    // D2
    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable UUID id,
                                               @RequestParam DrugOrderStatus status) {
        String msg = drugOrderService.changeStatus(id, status);
        if (msg.equals("Drug order status updated successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } else if (msg.equals("The drug order with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);   // "Cannot change status from X to Y"
    }

    // F1
    @GetMapping(value = "/stats/by-status")
    public ResponseEntity<List<Object[]>> statsByStatus() {
        return new ResponseEntity<>(drugOrderService.countByStatus(), HttpStatus.OK);
    }

    // F2
    @PatchMapping(value = "/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID pharmacistId, @RequestParam String date) {
        String msg = drugOrderService.cancelDay(pharmacistId, LocalDate.parse(date));
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    // Bonus: the Pageable is built here from the three parameters
    @GetMapping(value = "/page")
    public ResponseEntity<Page<DrugOrder>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderDate,asc") String sort) {
        String[] parts = sort.split(",");
        Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));
        return new ResponseEntity<>(drugOrderService.getPage(pageable), HttpStatus.OK);
    }
}