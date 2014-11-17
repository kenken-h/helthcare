package com.itrane.healthcare.command;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;

/**
 * vod 一覧ビュー（vodlist.html）用コマンド. 
 */
public class VodListCmd implements Serializable {

	private static final long serialVersionUID = 1L;

	private String prev;
	private String next;
	private List<VitalMst> vms;
	private List<Vod> vods;
	private DateTime startDt;
	
	public VodListCmd() {}

	public VodListCmd(String prev, String next,
			List<VitalMst> vms, List<Vod> vods, DateTime dt) {
		super();
		this.prev = prev;
		this.next = next;
		this.vms = vms;
		this.vods = vods;
		this.startDt = dt;
	}

	public String getPrev() {
		return prev;
	}

	public void setPrev(String prev) {
		this.prev = prev;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public List<VitalMst> getVms() {
		return vms;
	}

	public void setVms(List<VitalMst> vms) {
		this.vms = vms;
	}

	public List<Vod> getVods() {
		return vods;
	}

	public void setVods(List<Vod> vods) {
		this.vods = vods;
	}
	
	public DateTime getStartDt() {
		return startDt;
	}
	
	public void setStartDt(DateTime startDt) {
		this.startDt = startDt;
	}
}
