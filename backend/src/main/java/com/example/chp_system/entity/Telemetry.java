package com.example.chp_system.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chpUnitId;
    private double electricityKwh;
    private double heatGj;
    private double hydrogenUsedKg;
    private LocalDateTime readingTime;

    public Telemetry() {
    }

    public Telemetry(Long chpUnitId,
                     double electricityKwh,
                     double heatGj,
                     double hydrogenUsedKg,
                     LocalDateTime readingTime) {

        this.chpUnitId = chpUnitId;
        this.electricityKwh = electricityKwh;
        this.heatGj = heatGj;
        this.hydrogenUsedKg = hydrogenUsedKg;
        this.readingTime = readingTime;
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

    public double getHydrogenUsedKg() {
        return hydrogenUsedKg;
    }

    public void setHydrogenUsedKg(double hydrogenUsedKg) {
        this.hydrogenUsedKg = hydrogenUsedKg;
    }

    public LocalDateTime getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(LocalDateTime readingTime) {
        this.readingTime = readingTime;
    }
}