package com.example.chp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chp_system.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}