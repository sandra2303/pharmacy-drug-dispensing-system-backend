package com.pharmacy.dispensing.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dispensing.domain.DrugClass;
import com.pharmacy.dispensing.service.DrugClassService;

@RestController
@RequestMapping(value = "/api/drug-classes")
public class DrugClassController {

    @Autowired
    private DrugClassService drugClassService;

    // A2
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDrugClass(@RequestBody DrugClass drugClass) {
        String msg = drugClassService.saveDrugClass(drugClass);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DrugClass>> getAllDrugClasss() {
        return new ResponseEntity<>(drugClassService.getAllDrugClasss(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDrugClassById(@PathVariable UUID id) {
        Optional<DrugClass> drugClassOpt = drugClassService.getDrugClassById(id);
        if (drugClassOpt.isPresent()) {
            return new ResponseEntity<>(drugClassOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The drug class with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateDrugClass(@PathVariable UUID id, @RequestBody DrugClass drugClass) {
        String msg = drugClassService.updateDrugClass(id, drugClass);
        if (msg.equals("Drug class updated successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDrugClass(@PathVariable UUID id) {
        String msg = drugClassService.deleteDrugClass(id);
        if (msg.equals("Drug class deleted successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    // C3
    @GetMapping(value = "/unused")
    public ResponseEntity<List<DrugClass>> unused() {
        return new ResponseEntity<>(drugClassService.findUnusedDrugClasses(), HttpStatus.OK);
    }
}