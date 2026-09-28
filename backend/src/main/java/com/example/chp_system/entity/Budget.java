package com.example.chp_system.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chpUnitId;
    private String analyticAccount;
    private double plannedHydrogenCost;
    private double actualHydrogenCost;
    private double plannedRevenue;
    private double actualRevenue;
    private double variance;

    public Budget() {
    }

    public Budget(Long chpUnitId, String analyticAccount,
                   double plannedHydrogenCost,
                   double actualHydrogenCost,
                   double plannedRevenue,
                   double actualRevenue,
                   double variance) {

        this.chpUnitId = chpUnitId;
        this.analyticAccount = analyticAccount;
        this.plannedHydrogenCost = plannedHydrogenCost;
        this.actualHydrogenCost = actualHydrogenCost;
        this.plannedRevenue = plannedRevenue;
        this.actualRevenue = actualRevenue;
        this.variance = variance;
    }

    public Long getId() {
        return id;
    }

    public Long getChpUnitId() {
        return chpUnitId;
    }

    public void setChpUnitId(Long chpUnitId) {
        this.chpUnitId = chpUnitId;
    }

    public String getAnalyticAccount() {
        return analyticAccount;
    }

    public void setAnalyticAccount(String analyticAccount) {
        this.analyticAccount = analyticAccount;
    }

    public double getPlannedHydrogenCost() {
        return plannedHydrogenCost;
    }

    public void setPlannedHydrogenCost(double plannedHydrogenCost) {
        this.plannedHydrogenCost = plannedHydrogenCost;
    }

    public double getActualHydrogenCost() {
        return actualHydrogenCost;
    }

    public void setActualHydrogenCost(double actualHydrogenCost) {
        this.actualHydrogenCost = actualHydrogenCost;
    }

    public double getPlannedRevenue() {
        return plannedRevenue;
    }

    public void setPlannedRevenue(double plannedRevenue) {
        this.plannedRevenue = plannedRevenue;
    }

    public double getActualRevenue() {
        return actualRevenue;
    }

    public void setActualRevenue(double actualRevenue) {
        this.actualRevenue = actualRevenue;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }
}