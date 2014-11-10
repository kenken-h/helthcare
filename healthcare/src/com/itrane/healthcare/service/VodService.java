package com.itrane.healthcare.service;

import java.util.List;

import javax.persistence.EntityManager;

import com.itrane.healthcare.model.Vod;

public interface VodService {
	
	public Vod create(Vod vod);
	public Vod update(Vod vod);
	public Vod delete(long id) throws VodNotFound;
	
	public List<Vod> findBySokuteiBi(String sokuteiBi);
	public List<Vod> findBySokuteiBiBetween(String sokuteiBi1, String sokuteiBi2);
	
	public EntityManager getEm();
	
}
