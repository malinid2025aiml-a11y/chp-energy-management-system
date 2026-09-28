package com.example.chp_system.entity;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private double electricityKwh;
    private double heatGj;
    private double electricityRate;
    private double heatRate;
    private double electricityAmount;
    private double heatAmount;
    private double totalAmount;
    private String paymentStatus;
    private LocalDate invoiceDate;

    public Invoice() {
    }

    public Invoice(Long customerId,
                    double electricityKwh,
                    double heatGj,
                    double electricityRate,
                    double heatRate,
                    double electricityAmount,
                    double heatAmount,
                    double totalAmount,
                    String paymentStatus,
                    LocalDate invoiceDate) {

        this.customerId = customerId;
        this.electricityKwh = electricityKwh;
        this.heatGj = heatGj;
        this.electricityRate = electricityRate;
        this.heatRate = heatRate;
        this.electricityAmount = electricityAmount;
        this.heatAmount = heatAmount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.invoiceDate = invoiceDate;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public double getElectricityKwh() {
        return electricityKwh;
    }

    public void setElectricityKwh(double electricityKwh) {
        this.electricityKwh = electricityKwh;
    }

    public double getHeatGj() {
        return heatGj;
    }

    public void setHeatGj(double heatGj) {
        this.heatGj = heatGj;
    }

    public double getElectricityRate() {
        return electricityRate;
    }

    public void setElectricityRate(double electricityRate) {
        this.electricityRate = electricityRate;
    }

    public double getHeatRate() {
        return heatRate;
    }

    public void setHeatRate(double heatRate) {
        this.heatRate = heatRate;
    }

    public double getElectricityAmount() {
        return electricityAmount;
    }

    public void setElectricityAmount(double electricityAmount) {
        this.electricityAmount = electricityAmount;
    }

    public double getHeatAmount() {
        return heatAmount;
    }

    public void setHeatAmount(double heatAmount) {
        this.heatAmount = heatAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}