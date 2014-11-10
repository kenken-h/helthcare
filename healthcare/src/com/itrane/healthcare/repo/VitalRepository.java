package com.itrane.healthcare.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itrane.healthcare.model.Vital;

public interface VitalRepository extends JpaRepository<Vital, Long> {
	
}
