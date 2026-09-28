package com.example.chp_system.entity;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long supplierId;
    private String purchaseOrderNumber;
    private double hydrogenQuantityKg;
    private double pricePerKg;
    private double totalAmount;
    private String vendorBillNumber;
    private String paymentStatus;
    private LocalDate purchaseDate;

    public Purchase() {
    }

    public Purchase(Long supplierId,
                     String purchaseOrderNumber,
                     double hydrogenQuantityKg,
                     double pricePerKg,
                     double totalAmount,
                     String vendorBillNumber,
                     String paymentStatus,
                     LocalDate purchaseDate) {

        this.supplierId = supplierId;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.hydrogenQuantityKg = hydrogenQuantityKg;
        this.pricePerKg = pricePerKg;
        this.totalAmount = totalAmount;
        this.vendorBillNumber = vendorBillNumber;
        this.paymentStatus = paymentStatus;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() {
        return id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public double getHydrogenQuantityKg() {
        return hydrogenQuantityKg;
    }

    public void setHydrogenQuantityKg(double hydrogenQuantityKg) {
        this.hydrogenQuantityKg = hydrogenQuantityKg;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getVendorBillNumber() {
        return vendorBillNumber;
    }

    public void setVendorBillNumber(String vendorBillNumber) {
        this.vendorBillNumber = vendorBillNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}