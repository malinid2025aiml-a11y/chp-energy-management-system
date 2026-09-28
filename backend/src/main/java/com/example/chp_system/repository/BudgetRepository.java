package com.example.chp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chp_system.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}