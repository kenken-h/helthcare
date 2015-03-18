package com.itrane.healthcare.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.itrane.healthcare.model.UserInfo;

/**
 * UserDetailsService を拡張したユーザー・サービス.
 */
public interface UserService extends UserDetailsService {
	
	public List<UserInfo> findPage(int offset, int pageRows,
			String searchStr, String sortDir, String...sortCols);
	public UserInfo save(UserInfo e) throws DbAccessException;
}
