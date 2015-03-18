package com.itrane.healthcare.command;

import java.util.HashMap;
import java.util.Map;

import com.itrane.healthcare.model.UserInfo;

/**
 * user/userForm.html(ユーザー情報入力フォーム）用コマンド
 */
public class UserCmd {
    
	//ユーザ情報
	private UserInfo user;
        
	//制御ステータス： 1:保存成功 | 9:検証エラー | 0:初期状態
	private int status; 
	//表示ページ：0基準
	private long page;
	
	//メッセージ
	private String message;	
	private String userName;
	private Map<String, String> fldErrors;
	
	public UserCmd(UserInfo user) {
		super();
		this.user = user;
		this.message = "";
		this.status = 0;
		this.fldErrors = new HashMap<String, String>();
	}

	public UserCmd(int status, String message) {
		this.message = message;
		this.status = status;
		this.fldErrors = new HashMap<String, String>();
	}
	
	public UserCmd() {
		this(0, "");
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public Map<String, String> getFldErrors() {
		return fldErrors;
	}

	public void setFldErrors(Map<String, String> fldErrors) {
		this.fldErrors = fldErrors;
	}
}
