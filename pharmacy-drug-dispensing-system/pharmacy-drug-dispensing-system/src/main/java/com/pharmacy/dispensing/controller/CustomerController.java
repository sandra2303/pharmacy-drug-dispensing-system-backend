package com.pharmacy.dispensing.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dispensing.domain.Customer;
import com.pharmacy.dispensing.service.CustomerService;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // A2
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        String msg = customerService.saveCustomer(customer);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable UUID id) {
        Optional<Customer> customerOpt = customerService.getCustomerById(id);
        if (customerOpt.isPresent()) {
            return new ResponseEntity<>(customerOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The customer with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable UUID id, @RequestBody Customer customer) {
        String msg = customerService.updateCustomer(id, customer);
        if (msg.equals("Customer updated successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable UUID id) {
        String msg = customerService.deleteCustomer(id);
        if (msg.equals("Customer deleted successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    // B1
    @GetMapping(value = "/by-last-name")
    public ResponseEntity<List<Customer>> byLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(customerService.findByLastName(lastName), HttpStatus.OK);
    }

    // C4
    @GetMapping(value = "/of-pharmacist/{pharmacistId}")
    public ResponseEntity<?> customersOfPharmacist(@PathVariable UUID pharmacistId) {
        Optional<List<Customer>> customers = customerService.getCustomersOfPharmacist(pharmacistId);
        if (customers.isPresent()) {
            return new ResponseEntity<>(customers.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The pharmacist with that id does not exist", HttpStatus.NOT_FOUND);
    }
}