package com.pharmacy.dispensing.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.dispensing.service.DashboardService;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Bonus +5
    @GetMapping(value = "/summary")
    public ResponseEntity<Map<String, Long>> summary() {
        return new ResponseEntity<>(dashboardService.getSummary(), HttpStatus.OK);
    }
}
