package com.itrane.healthcare.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.repo.VitalRepository;

@Service
public class VitalServiceImpl implements VitalService {
	
	@Resource
	private VitalRepository vitalRepository;
	
	public VitalServiceImpl() {}
	
	public VitalServiceImpl(VitalRepository repo) {
		this.vitalRepository = repo;
	}

	@Override
	public Vital findById(long id) {
		return vitalRepository.findOne(id);
	}

}
