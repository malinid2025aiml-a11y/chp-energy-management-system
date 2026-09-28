package com.example.chp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chp_system.entity.Telemetry;

public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {
}