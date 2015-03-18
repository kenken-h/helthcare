package com.itrane.healthcare.service;

import java.util.List;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.VitalMst;

public interface VitalMstService {
	
	public VitalMst create(VitalMst e);
	public VitalMst delete(long id) throws EntityNotFoundException;
	public VitalMst update(VitalMst e) throws EntityNotFoundException;

	public List<VitalMst> findByPage(UserInfo user, 
			int offset, int pageRows, String searchStr, String sortDir, String... cols);
	public List<VitalMst> findByUserName(String userName);

}
