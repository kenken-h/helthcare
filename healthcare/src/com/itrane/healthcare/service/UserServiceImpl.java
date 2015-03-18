package com.itrane.healthcare.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalMstRepository;

/**
 * ログイン・ユーザー(UserDetails)と患者(Patient)のサービス実装.
 */
@Service
public class UserServiceImpl implements UserService {

	//final private Logger log = LoggerFactory.getLogger(getClass());

	@Resource private UserRepository repo;
	@Resource private VitalMstRepository vmRepo;

	/**
	 * セキュリティ設定で認証プロバイダにこのサービスを使う設定を行った場合、
	 * プロバイダはこのメソッドを実行して認証を行う。
	 * 認証されるためには、ログインページで入力された username に対して、
	 * 適切な User を返す必要がある。
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		if (username.equals("user1")) {
			//開発時の管理者ログインの認証
			List<String> roles = new ArrayList<String>();
			roles.add("ROLE_ADMIN");
			ShaPasswordEncoder spe = new ShaPasswordEncoder(256);
			String epass = spe.encodePassword("user1", null);
			User user = new User("user1", epass, true, true, true, true,
					getGrantedAuthorities(roles));
			return user;
		} 
		
		{	//製品版でのログイン認証
			List<UserInfo> users = repo
					.findByName(username);
			if (users == null || users.size() == 0) {
				throw new UsernameNotFoundException("");
			} else {
				UserInfo patient = users.get(0);
				User user = new User(patient.getName(),
						patient.getPass(), true, true, true, true,
						getGrantedAuthorities(getRoles(patient.getRoles())));
				return user;
			}
		}
	}

	private List<String> getRoles(String roles) {
		if (roles != null) {
			return Arrays.asList(roles.split(","));
		} else {
			return new ArrayList<String>();
		}
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	@Override
	public List<UserInfo> findPage(int offset, int pageRows, 
			String searchStr, String sortDir, String... sortCols) {
		Sort.Direction dir = Sort.Direction.ASC;
		if (sortDir.equals("desc")) {
			dir = Sort.Direction.DESC;
		}
		int pageNum = offset / pageRows;
		PageRequest request = new PageRequest(pageNum, pageRows, dir, sortCols);
		
		if (searchStr.equals("")) {
			return repo.findPage(request);
		} else {
			return repo.findPage(searchStr, request);
		}
	}

	@Override
	public UserInfo save(UserInfo e) throws DbAccessException {
		UserInfo p = null;
		try {
			p = repo.save(e);
		} catch (Exception ex) {
			for(Throwable t = ex.getCause(); t != null; t = t.getCause()) {
				if (t instanceof OptimisticLockException) {
					throw new DbAccessException("楽観的ロックエラー");
				} else if (t instanceof SQLException) {
					SQLException sqlex = (SQLException) t;  //ルート例外を SQL Exceptionでキャスト
					if(sqlex.getSQLState().equals("23000") && sqlex.getErrorCode()==1062) {
						throw new DbAccessException("重複エラー");
					} 
				}
			}
			throw new DbAccessException("その他のDBエラー");
		}
		return p;
	}

}
