package com.itrane.healthcare.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.Vod;

public interface VodRepository extends JpaRepository<Vod, Long> {
	
	@Query("select v from Vod v where v.user=:user and v.sokuteiBi=:sokuteiBi")
	public List<Vod> findBySokuteiBi(@Param("user") UserInfo user,
			@Param("sokuteiBi") String sokuteiBi);
	
	@Query("select v from Vod v where v.user=:user and (v.sokuteiBi " +
			"between :sokuteiBi1 and :sokuteiBi2) order by v.sokuteiBi")
	public List<Vod> findBySokuteiBiBetween(@Param("user") UserInfo user,
			@Param("sokuteiBi1") String sokuteiBi1, @Param("sokuteiBi2") String sokuteiBi2);
	
	@Query("select v from Vod v where v.user=:user")
	public List<Vod> findAllByUser(@Param("user") UserInfo user);
	
	
}
