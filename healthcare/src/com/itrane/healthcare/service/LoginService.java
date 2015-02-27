package com.itrane.healthcare.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
	
	
	public LoginService() {};

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		ShaPasswordEncoder spe = new ShaPasswordEncoder(256);
		if (userName.equals("admin")) {
			List<String> roles = new ArrayList<String>();
			roles.add("ROLE_ADMIN");
			User user = new User(
					"admin",
					spe.encodePassword("admin", null),
					true, true, true, true, 
					getGrantedAuthorities(roles)
					);
			return user;
		} else if(userName.equals("user")) {
			List<String> roles = new ArrayList<String>();
			roles.add("ROLE_USER");
			User user = new User(
					"user",
					spe.encodePassword("user", null),
					true, true, true, true, 
					getGrantedAuthorities(roles)
					);
			return user;
			
		} else {
			throw new UsernameNotFoundException("");
		}
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}



}
