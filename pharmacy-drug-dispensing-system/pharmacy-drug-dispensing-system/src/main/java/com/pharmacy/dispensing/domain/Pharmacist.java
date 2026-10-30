package com.pharmacy.dispensing.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pharmacist")
public class Pharmacist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "license_number")
    private String licenseNumber;

    // may be null
    @ManyToOne
    @JoinColumn(name = "counter_id")
    private Counter counter;

    @ManyToMany
    @JoinTable(
        name = "pharmacist_drug_class",
        joinColumns = @JoinColumn(name = "pharmacist_id"),
        inverseJoinColumns = @JoinColumn(name = "drug_class_id")
    )
    private List<DrugClass> drugClasses = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public List<DrugClass> getDrugClasses() {
        return drugClasses;
    }

    public void setDrugClasses(List<DrugClass> drugClasses) {
        this.drugClasses = drugClasses;
    }
}