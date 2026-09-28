package com.example.chp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chp_system.entity.ChpUnit;

public interface ChpUnitRepository extends JpaRepository<ChpUnit, Long> {
}