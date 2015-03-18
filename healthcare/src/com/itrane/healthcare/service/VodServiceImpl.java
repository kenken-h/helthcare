package com.itrane.healthcare.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalRepository;
import com.itrane.healthcare.repo.VodRepository;

@Service
public class VodServiceImpl implements VodService {
	
	//static final private Logger log = LoggerFactory.getLogger(VodServiceImpl.class);
	
	@Resource private VodRepository vodRepository;
	@Resource private VitalRepository vitalRepository;
	@Resource private UserRepository userRepo;
	
	@PersistenceContext private EntityManager em;
	
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
	@Transactional(rollbackFor=EntityNotFoundException.class)
	public Vod update(Vod vod)  {
		for(Vital vt: vod.getVitals()) {
			if (vt.getSokuteiJikan() != null) {
				vitalRepository.save(vt); 
			}
		}
		return vodRepository.save(vod);
	}
	
	@Override
	@Transactional(rollbackFor=EntityNotFoundException.class)
	public Vod delete(Vod e) throws EntityNotFoundException {
		Vod deletedVod = vodRepository.findOne(e.getId());
		
		if (deletedVod == null)
			throw new EntityNotFoundException();
		
		vodRepository.delete(deletedVod);
		return deletedVod;
	}

	@Override
	public List<Vod> findBySokuteiBi(UserInfo user, String sokuteiBi) {
		return vodRepository.findBySokuteiBi(user, sokuteiBi);
	}

	@Override
	public List<Vod> findBySokuteiBiBetween(UserInfo user, String sokuteiBi1, String sokuteiBi2) {
		return vodRepository.findBySokuteiBiBetween(user, sokuteiBi1, sokuteiBi2);
	}
	
	public List<Vod> findBySokuteiBiBetween(String userName, String sokuteiBi1, String sokuteiBi2) {
		List<UserInfo> pts = userRepo.findByName(userName);
		if (pts.size() == 1) {
			return vodRepository.findBySokuteiBiBetween(pts.get(0), sokuteiBi1, sokuteiBi2);
		} else {
			return new ArrayList<Vod>();
		}
	}
}
