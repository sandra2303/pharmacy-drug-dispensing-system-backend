package com.pharmacy.dispensing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dispensing.domain.Counter;
import com.pharmacy.dispensing.repository.CounterRepository;

@Service
public class CounterService {

    @Autowired
    private CounterRepository counterRepo;

    public String saveCounter(Counter counter) {
        counterRepo.save(counter);
        return "Counter saved successfully";
    }

    public List<Counter> getAllCounters() {
        return counterRepo.findAll();
    }

    public Optional<Counter> getCounterById(UUID id) {
        return counterRepo.findById(id);
    }

    public String updateCounter(UUID id, Counter updated) {
        Optional<Counter> existing = counterRepo.findById(id);
        if (existing.isEmpty()) {
            return "The counter with that id does not exist";
        }
        Counter counter = existing.get();
        counter.setCounterNumber(updated.getCounterNumber());
        counter.setName(updated.getName());
        counterRepo.save(counter);
        return "Counter updated successfully";
    }

    public String deleteCounter(UUID id) {
        if (!counterRepo.existsById(id)) {
            return "The counter with that id does not exist";
        }
        counterRepo.deleteById(id);
        return "Counter deleted successfully";
    }

    // C2
    public List<Counter> findFreeCounters() {
        return counterRepo.findFreeCounters();
    }
}