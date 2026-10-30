package com.pharmacy.dispensing.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dispensing.domain.Counter;
import com.pharmacy.dispensing.service.CounterService;

@RestController
@RequestMapping(value = "/api/counters")
public class CounterController {

    @Autowired
    private CounterService counterService;

    // A2
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveCounter(@RequestBody Counter counter) {
        String msg = counterService.saveCounter(counter);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Counter>> getAllCounters() {
        return new ResponseEntity<>(counterService.getAllCounters(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCounterById(@PathVariable UUID id) {
        Optional<Counter> counterOpt = counterService.getCounterById(id);
        if (counterOpt.isPresent()) {
            return new ResponseEntity<>(counterOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The counter with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateCounter(@PathVariable UUID id, @RequestBody Counter counter) {
        String msg = counterService.updateCounter(id, counter);
        if (msg.equals("Counter updated successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCounter(@PathVariable UUID id) {
        String msg = counterService.deleteCounter(id);
        if (msg.equals("Counter deleted successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    // C2
    @GetMapping(value = "/free")
    public ResponseEntity<List<Counter>> freeCounters() {
        return new ResponseEntity<>(counterService.findFreeCounters(), HttpStatus.OK);
    }
}