package com.pharmacy.dispensing.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dispensing.domain.Pharmacist;
import com.pharmacy.dispensing.service.PharmacistService;

@RestController
@RequestMapping(value = "/api/pharmacists")
public class PharmacistController {

    @Autowired
    private PharmacistService pharmacistService;

    // A2
    @PostMapping(value = "/save")
    public ResponseEntity<?> savePharmacist(@RequestBody Pharmacist pharmacist) {
        String msg = pharmacistService.savePharmacist(pharmacist);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Pharmacist>> getAllPharmacists() {
        return new ResponseEntity<>(pharmacistService.getAllPharmacists(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPharmacistById(@PathVariable UUID id) {
        Optional<Pharmacist> pharmacistOpt = pharmacistService.getPharmacistById(id);
        if (pharmacistOpt.isPresent()) {
            return new ResponseEntity<>(pharmacistOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The pharmacist with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePharmacist(@PathVariable UUID id, @RequestBody Pharmacist pharmacist) {
        String msg = pharmacistService.updatePharmacist(id, pharmacist);
        if (msg.equals("Pharmacist updated successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePharmacist(@PathVariable UUID id) {
        String msg = pharmacistService.deletePharmacist(id);
        if (msg.equals("Pharmacist deleted successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    // C1
    @GetMapping(value = "/by-drug-class")
    public ResponseEntity<List<Pharmacist>> byDrugClass(@RequestParam String name) {
        return new ResponseEntity<>(pharmacistService.findByDrugClass(name), HttpStatus.OK);
    }

    // D3
    @PutMapping(value = "/{pharmacistId}/drug-classes/{drugClassId}")
    public ResponseEntity<String> addDrugClass(@PathVariable UUID pharmacistId,
                                               @PathVariable UUID drugClassId) {
        String msg = pharmacistService.addDrugClass(pharmacistId, drugClassId);
        if (msg.equals("Drug class added to the pharmacist")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } else if (msg.equals("The pharmacist already has that drug class")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    // D4
    @PatchMapping(value = "/{pharmacistId}/counter")
    public ResponseEntity<String> changeCounter(@PathVariable UUID pharmacistId,
                                                @RequestParam int counterNumber) {
        String msg = pharmacistService.changeCounter(pharmacistId, counterNumber);
        if (msg.equals("Counter changed successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } else if (msg.equals("That counter is taken by another pharmacist")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
}