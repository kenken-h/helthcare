package com.itrane.healthcare.service;

import java.util.List;

import javax.persistence.EntityManager;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.Vod;

public interface VodService {
	
	public Vod create(Vod e);
	public Vod update(Vod e);
	public Vod delete(Vod e) throws EntityNotFoundException;
	
	public List<Vod> findBySokuteiBi(UserInfo user, String sokuteiBi);
	public List<Vod> findBySokuteiBiBetween(UserInfo user, String sokuteiBi1, String sokuteiBi2);
	public List<Vod> findBySokuteiBiBetween(String userName, String sokuteiBi1, String sokuteiBi2);
	
	public EntityManager getEm();
	
}
