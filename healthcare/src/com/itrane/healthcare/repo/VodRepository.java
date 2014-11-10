package com.itrane.healthcare.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itrane.healthcare.model.Vod;

public interface VodRepository extends JpaRepository<Vod, Long> {
	
	@Query("select v from Vod v where v.sokuteiBi = ?1")
	public List<Vod> findBySokuteiBi(String sokuteiBi);
	@Query("select v from Vod v where v.sokuteiBi between ?1 and ?2 order by v.sokuteiBi")
	public List<Vod> findBySokuteiBiBetween(String sokuteiBi1, String sokuteiBi2);
	
}
