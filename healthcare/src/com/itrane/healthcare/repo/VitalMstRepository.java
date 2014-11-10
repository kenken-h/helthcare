package com.itrane.healthcare.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itrane.healthcare.model.VitalMst;

public interface VitalMstRepository extends JpaRepository<VitalMst, Long> {
	
	@Query("select v from VitalMst v order by v.junjo")
	public List<VitalMst> findAllOrderByJunjo(); 
	
}
