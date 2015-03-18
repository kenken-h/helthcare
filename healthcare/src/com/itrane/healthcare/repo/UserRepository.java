package com.itrane.healthcare.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itrane.healthcare.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
	
	@Query(value="select u from UserInfo u where u.name=:userName")
	public List<UserInfo> findByName(@Param("userName") String userName);
	
	//$$$ ページ取得 $$$$$
	//検索文字列あり
	@Query("select u from UserInfo u where " + 
			"concat(u.name, u.simeiSei, u.simeiMei) like %:searchStr%")
	public List<UserInfo> findPage(@Param("searchStr") String searchStr, Pageable request);

	//検索文字列なし
	@Query("select u from UserInfo u")
	public List<UserInfo> findPage(Pageable request);

}
