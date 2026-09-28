package com.example.chp_system.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChpUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unitName;
    private String location;
    private double electricityCapacity;
    private double heatCapacity;
    private double heatToPowerRatio;
    private double electricityTariff;
    private double heatTariff;
    private String status;

    public ChpUnit() {
    }

    public ChpUnit(String unitName, String location,
                   double electricityCapacity,
                   double heatCapacity,
                   double heatToPowerRatio,
                   double electricityTariff,
                   double heatTariff,
                   String status) {

        this.unitName = unitName;
        this.location = location;
        this.electricityCapacity = electricityCapacity;
        this.heatCapacity = heatCapacity;
        this.heatToPowerRatio = heatToPowerRatio;
        this.electricityTariff = electricityTariff;
        this.heatTariff = heatTariff;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getElectricityCapacity() {
        return electricityCapacity;
    }

    public void setElectricityCapacity(double electricityCapacity) {
        this.electricityCapacity = electricityCapacity;
    }

    public double getHeatCapacity() {
        return heatCapacity;
    }

    public void setHeatCapacity(double heatCapacity) {
        this.heatCapacity = heatCapacity;
    }

    public double getHeatToPowerRatio() {
        return heatToPowerRatio;
    }

    public void setHeatToPowerRatio(double heatToPowerRatio) {
        this.heatToPowerRatio = heatToPowerRatio;
    }

    public double getElectricityTariff() {
        return electricityTariff;
    }

    public void setElectricityTariff(double electricityTariff) {
        this.electricityTariff = electricityTariff;
    }

    public double getHeatTariff() {
        return heatTariff;
    }

    public void setHeatTariff(double heatTariff) {
        this.heatTariff = heatTariff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}