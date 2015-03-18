package com.itrane.healthcare.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * ユーザー情報エンティティ.
 * お薬手帳の個人情報.
 */
@Entity
@Table(name="user")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final ShaPasswordEncoder spe = new ShaPasswordEncoder(256);	//SHA-256
	
	@Id
	@GeneratedValue
	private Long id = null;
	
	@Version
    protected Integer version = 0;

	//ログイン情報
	@Column(unique=true)
	@Length(min=4, max=12, message="ユーザー名は{min}から{max}文字の範囲で入力してください。")
	private String name;
	//@Length(min=4, message="パスワードは{min}文字以上の文字列を入力してください。")
	@Transient
	private String ipass;
    private String pass;
    private String roles;

	private String simeiSei;
	private String simeiMei;

	/*
	 * TODO : 既往歴、薬局、病院などの情報を追加する.
	 */

    @Transient
    private List<String> roleList;
            
	public UserInfo(String name, String ipass, String simeiSei, String simeiMei, String roles) {
		super();
		this.name = name;
		setIpass(ipass);
		this.simeiSei = simeiSei;
		this.simeiMei = simeiMei;
		this.roles = roles;
	}
	
	public UserInfo(String name, String pass) {
		this(name, pass, "", "", "");
	}
	
	public UserInfo() {
		this("", "", "", "", "");
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getIpass() {
		return ipass;
	}

	public void setIpass(String ipass) {
		this.ipass = ipass;
		this.pass = spe.encodePassword(ipass, null);
	}

	public String getSimeiSei() {
		return simeiSei;
	}

	public void setSimeiSei(String simeiSei) {
		this.simeiSei = simeiSei;
	}

	public String getSimeiMei() {
		return simeiMei;
	}

	public void setSimeiMei(String simeiMei) {
		this.simeiMei = simeiMei;
	}


	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
		if (roles!=null && roles.length() > 0) {
			this.roleList = Arrays.asList(roles.split(",", 0));
		}
	}

	public List<String> getRoleList() {
		if (this.roleList==null) {
			this.roleList = new ArrayList<String>();
		}
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", version=" + version + ", name=" + name
				+ ", ipass=" + ipass + ", pass=" + pass + ", roles=" + roles
				+ ", simeiSei=" + simeiSei + ", simeiMei=" + simeiMei
				+ ", roleList=" + roleList + "]";
	}

}
