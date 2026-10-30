package com.pharmacy.dispensing.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dispensed_item")
public class DispensedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "drug_name")
    private String drugName;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "dispensed_date")
    private LocalDate dispensedDate;

    // one drug order can have many dispensed items (no List in DrugOrder)
    @ManyToOne
    @JoinColumn(name = "drug_order_id")
    private DrugOrder drugOrder;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDispensedDate() {
        return dispensedDate;
    }

    public void setDispensedDate(LocalDate dispensedDate) {
        this.dispensedDate = dispensedDate;
    }

    public DrugOrder getDrugOrder() {
        return drugOrder;
    }

    public void setDrugOrder(DrugOrder drugOrder) {
        this.drugOrder = drugOrder;
    }
}