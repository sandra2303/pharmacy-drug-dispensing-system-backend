package com.pharmacy.dispensing.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dispensing.domain.DispensedItem;
import com.pharmacy.dispensing.service.DispensedItemService;

@RestController
@RequestMapping(value = "/api/dispensed-items")
public class DispensedItemController {

    @Autowired
    private DispensedItemService dispensedItemService;

    // E1
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDispensedItem(@RequestParam UUID drugOrderId,
                                               @RequestBody DispensedItem dispensedItem) {
        String msg = dispensedItemService.saveDispensedItem(drugOrderId, dispensedItem);
        if (msg.equals("Dispensed item saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else if (msg.equals("The drug order with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);   // not a DISPENSED drug order
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DispensedItem>> getAllDispensedItems() {
        return new ResponseEntity<>(dispensedItemService.getAllDispensedItems(), HttpStatus.OK);
    }

    // E2
    @GetMapping(value = "/customer/{customerNumber}")
    public ResponseEntity<List<DispensedItem>> byCustomer(@PathVariable String customerNumber) {
        return new ResponseEntity<>(dispensedItemService.findByCustomerNumber(customerNumber), HttpStatus.OK);
    }

    // E3
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDispensedItem(@PathVariable UUID id) {
        String msg = dispensedItemService.deleteDispensedItem(id);
        if (msg.equals("Dispensed item deleted successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
}