package com.itrane.healthcare.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.VitalMst;

public interface VitalMstRepository extends JpaRepository<VitalMst, Long> {	
	
	@Query("select v from VitalMst v where v.user=:user")
	public List<VitalMst> findAllByUser(@Param("user") UserInfo user); 
	
	@Query("select v from VitalMst v where v.user=:user and " +
	        "concat(v.name, v.jikan) like %:searchStr%")
	public List<VitalMst> findPage(@Param("user") UserInfo user,
			@Param("searchStr") String searchStr, Pageable pr);

	@Query("select v from VitalMst v where v.user=:user")
	public List<VitalMst> findPage(@Param("user") UserInfo user, Pageable pr);

	@Query("select count(v) from VitalMst v where v.user=:user")
	public int countByUser(@Param("user") UserInfo user);
	
	public Long removeByUser(String userName);
}
