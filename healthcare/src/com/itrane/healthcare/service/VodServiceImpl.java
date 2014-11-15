package com.itrane.healthcare.service;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.VitalRepository;
import com.itrane.healthcare.repo.VodRepository;

@Service
public class VodServiceImpl implements VodService {
	
	static final private Logger log = LoggerFactory.getLogger(VodServiceImpl.class);
	
	@Resource
	private VodRepository vodRepository;
	
	@Resource
	private VitalRepository vitalRepository;
	
	@PersistenceContext
	private EntityManager em;
	
	public VodServiceImpl() {}
	
	public VodServiceImpl(VodRepository repo) {
		this.vodRepository = repo;
	}
	
	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	@Transactional
	public Vod create(Vod vod) {
		return vodRepository.save(vod);
	}
	
	@Override
	@Transactional(rollbackFor=VodNotFound.class)
	public Vod update(Vod vod)  {
		for(Vital vt: vod.getVitals()) {
			if (vt.getSokuteiJikan() != null) {
				vitalRepository.save(vt); 
			}
		}
		return vodRepository.save(vod);
	}
	
	@Override
	@Transactional(rollbackFor=VodNotFound.class)
	public Vod delete(long id) throws VodNotFound {
		Vod deletedVod = vodRepository.findOne(id);
		
		if (deletedVod == null)
			throw new VodNotFound();
		
		vodRepository.delete(deletedVod);
		return deletedVod;
	}

	@Override
	public List<Vod> findBySokuteiBi(String sokuteiBi) {
		return vodRepository.findBySokuteiBi(sokuteiBi);
	}

	@Override
	public List<Vod> findBySokuteiBiBetween(String sokuteiBi1, String sokuteiBi2) {
		return vodRepository.findBySokuteiBiBetween(sokuteiBi1, sokuteiBi2);
	}
	
}
