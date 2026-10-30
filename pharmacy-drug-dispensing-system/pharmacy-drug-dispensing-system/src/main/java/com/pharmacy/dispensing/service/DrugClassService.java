package com.pharmacy.dispensing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dispensing.domain.DrugClass;
import com.pharmacy.dispensing.repository.DrugClassRepository;

@Service
public class DrugClassService {

    @Autowired
    private DrugClassRepository drugClassRepo;

    public String saveDrugClass(DrugClass drugClass) {
        drugClassRepo.save(drugClass);
        return "Drug class saved successfully";
    }

    public List<DrugClass> getAllDrugClasss() {
        return drugClassRepo.findAll();
    }

    public Optional<DrugClass> getDrugClassById(UUID id) {
        return drugClassRepo.findById(id);
    }

    public String updateDrugClass(UUID id, DrugClass updated) {
        Optional<DrugClass> existing = drugClassRepo.findById(id);
        if (existing.isEmpty()) {
            return "The drug class with that id does not exist";
        }
        DrugClass drugClass = existing.get();
        drugClass.setName(updated.getName());
        drugClassRepo.save(drugClass);
        return "Drug class updated successfully";
    }

    public String deleteDrugClass(UUID id) {
        if (!drugClassRepo.existsById(id)) {
            return "The drug class with that id does not exist";
        }
        drugClassRepo.deleteById(id);
        return "Drug class deleted successfully";
    }

    // C3
    public List<DrugClass> findUnusedDrugClasses() {
        return drugClassRepo.findUnusedDrugClasses();
    }
}