package com.example.chp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chp_system.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}