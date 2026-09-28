package com.example.chp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chp_system.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}