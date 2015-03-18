package com.itrane.healthcare.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalMstRepository;

@Service
public class VitalMstServiceImpl implements VitalMstService {
	
	//static final private Logger log = LoggerFactory.getLogger(VitalMstServiceImpl.class);
	
	@Resource private VitalMstRepository repo;
	@Resource private UserRepository userRepo;
	
	public VitalMstServiceImpl() {}
	
	/**
	 * コンストラクタ.
	 * @param repo
	 */
	public VitalMstServiceImpl(VitalMstRepository repo,
			UserRepository userRepo) {
		this.repo = repo;
		this.userRepo = userRepo;
	}

	/**
	 * 新規作成.
	 * @param vm 新規登録
	 */
	@Override
	@Transactional
	public VitalMst create(VitalMst e) {
		return repo.save(e);
	}
	
	@Override
	@Transactional(rollbackFor=EntityNotFoundException.class)
	public VitalMst delete(long id) throws EntityNotFoundException {
		VitalMst deleted = repo.findOne(id);
		
		if (deleted == null)
			throw new EntityNotFoundException();
		
		repo.delete(deleted);
		return deleted;
	}
	
	@Override
	@Transactional(rollbackFor=EntityNotFoundException.class)
	public VitalMst update(VitalMst e) throws EntityNotFoundException {
		VitalMst updated = repo.findOne(e.getId());
		
		if (updated == null)
			throw new EntityNotFoundException();
		
		updated.setName(e.getName());
		updated.setType(e.getType());
		updated.setJunjo(e.getJunjo());
		updated.setKijunMin(e.getKijunMin());
		updated.setKijunMax(e.getKijunMax());
		
		return updated;
	}


	@Override
	public List<VitalMst> findByPage(UserInfo user, int offset, int pageRows,
			String searchStr, String sortDir, String... cols) {
		Sort.Direction dir = Sort.Direction.ASC;
		if (sortDir.equals("desc")) {
			dir = Sort.Direction.DESC;
		}
		int pageNum = offset / pageRows;
		PageRequest request = new PageRequest(pageNum, pageRows, dir, cols);
		
		if (searchStr.equals("")) {
			return repo.findPage(user, request);
		} else {
			return repo.findPage(user, searchStr, request);
		}
	}
	
	@Override
	public List<VitalMst> findByUserName(String userName) {
		List<UserInfo> pts = userRepo.findByName(userName);
		if (pts.size() == 1) {
			return repo.findAllByUser(pts.get(0));
		} else {
			return new ArrayList<VitalMst>();
		}
	}

}
