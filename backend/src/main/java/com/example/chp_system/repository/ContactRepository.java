package com.example.chp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chp_system.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}