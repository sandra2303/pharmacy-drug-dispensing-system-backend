package com.pharmacy.dispensing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dispensing.domain.Customer;
import com.pharmacy.dispensing.repository.CustomerRepository;
import com.pharmacy.dispensing.repository.PharmacistRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PharmacistRepository pharmacistRepo;

    public String saveCustomer(Customer customer) {
        customerRepo.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepo.findById(id);
    }

    public String updateCustomer(UUID id, Customer updated) {
        Optional<Customer> existing = customerRepo.findById(id);
        if (existing.isEmpty()) {
            return "The customer with that id does not exist";
        }
        Customer customer = existing.get();
        customer.setFirstName(updated.getFirstName());
        customer.setLastName(updated.getLastName());
        customer.setCustomerNumber(updated.getCustomerNumber());
        customer.setInsuranceNumber(updated.getInsuranceNumber());
        customer.setPhone(updated.getPhone());
        customerRepo.save(customer);
        return "Customer updated successfully";
    }

    public String deleteCustomer(UUID id) {
        if (!customerRepo.existsById(id)) {
            return "The customer with that id does not exist";
        }
        customerRepo.deleteById(id);
        return "Customer deleted successfully";
    }

    // B1
    public List<Customer> findByLastName(String lastName) {
        return customerRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    // C4: empty Optional = pharmacist does not exist
    public Optional<List<Customer>> getCustomersOfPharmacist(UUID pharmacistId) {
        if (!pharmacistRepo.existsById(pharmacistId)) {
            return Optional.empty();
        }
        return Optional.of(customerRepo.findCustomersOfPharmacist(pharmacistId));
    }
}