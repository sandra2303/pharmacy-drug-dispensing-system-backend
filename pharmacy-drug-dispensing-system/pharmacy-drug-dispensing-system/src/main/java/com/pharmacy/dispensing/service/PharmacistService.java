package com.pharmacy.dispensing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dispensing.domain.Counter;
import com.pharmacy.dispensing.domain.DrugClass;
import com.pharmacy.dispensing.domain.Pharmacist;
import com.pharmacy.dispensing.repository.CounterRepository;
import com.pharmacy.dispensing.repository.DrugClassRepository;
import com.pharmacy.dispensing.repository.PharmacistRepository;

@Service
public class PharmacistService {

    @Autowired
    private PharmacistRepository pharmacistRepo;

    @Autowired
    private DrugClassRepository drugClassRepo;

    @Autowired
    private CounterRepository counterRepo;

    public String savePharmacist(Pharmacist pharmacist) {
        pharmacistRepo.save(pharmacist);
        return "Pharmacist saved successfully";
    }

    public List<Pharmacist> getAllPharmacists() {
        return pharmacistRepo.findAll();
    }

    public Optional<Pharmacist> getPharmacistById(UUID id) {
        return pharmacistRepo.findById(id);
    }

    public String updatePharmacist(UUID id, Pharmacist updated) {
        Optional<Pharmacist> existing = pharmacistRepo.findById(id);
        if (existing.isEmpty()) {
            return "The pharmacist with that id does not exist";
        }
        Pharmacist pharmacist = existing.get();
        pharmacist.setFirstName(updated.getFirstName());
        pharmacist.setLastName(updated.getLastName());
        pharmacist.setLicenseNumber(updated.getLicenseNumber());
        pharmacist.setCounter(updated.getCounter());
        pharmacistRepo.save(pharmacist);
        return "Pharmacist updated successfully";
    }

    public String deletePharmacist(UUID id) {
        if (!pharmacistRepo.existsById(id)) {
            return "The pharmacist with that id does not exist";
        }
        pharmacistRepo.deleteById(id);
        return "Pharmacist deleted successfully";
    }

    // C1
    public List<Pharmacist> findByDrugClass(String name) {
        return pharmacistRepo.findByDrugClassName(name);
    }

    // D3: add one drug class, keep the existing ones
    @Transactional
    public String addDrugClass(UUID pharmacistId, UUID drugClassId) {
        Optional<Pharmacist> pharmacistOpt = pharmacistRepo.findById(pharmacistId);
        if (pharmacistOpt.isEmpty()) {
            return "The pharmacist with that id does not exist";
        }
        Optional<DrugClass> drugClassOpt = drugClassRepo.findById(drugClassId);
        if (drugClassOpt.isEmpty()) {
            return "That drug class does not exist";
        }
        Pharmacist pharmacist = pharmacistOpt.get();
        DrugClass drugClass = drugClassOpt.get();

        if (pharmacist.getDrugClasses().contains(drugClass)) {
            return "The pharmacist already has that drug class";
        }
        pharmacist.getDrugClasses().add(drugClass);
        pharmacistRepo.save(pharmacist);   // Pharmacist is the owning side
        return "Drug class added to the pharmacist";
    }

    // D4: move a pharmacist to another counter
    public String changeCounter(UUID pharmacistId, int counterNumber) {
        Optional<Pharmacist> pharmacistOpt = pharmacistRepo.findById(pharmacistId);
        if (pharmacistOpt.isEmpty()) {
            return "The pharmacist with that id does not exist";
        }
        Optional<Counter> counterOpt = counterRepo.findByCounterNumber(counterNumber);
        if (counterOpt.isEmpty()) {
            return "The counter with that counterNumber does not exist";
        }
        Counter counter = counterOpt.get();

        if (pharmacistRepo.existsByCounterIdAndIdNot(counter.getId(), pharmacistId)) {
            return "That counter is taken by another pharmacist";
        }
        Pharmacist pharmacist = pharmacistOpt.get();
        pharmacist.setCounter(counter);
        pharmacistRepo.save(pharmacist);
        return "Counter changed successfully";
    }
}