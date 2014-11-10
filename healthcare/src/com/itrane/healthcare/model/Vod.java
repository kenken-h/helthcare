package com.itrane.healthcare.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.DateTime;

/**
 * １日分のバイタル測定値を表すクラス.
 */
@Entity
@Table(name = "vod")
public class Vod implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id = null;
	
	@Version
    protected Integer version = 0;
	
	@NotNull
	@Size(max=10)
	private String sokuteiBi;	
	private List<Vital> vitals = new ArrayList<Vital>();
	
	private String memo;
	
	/** コンストラクタ. */
	public Vod() {
		this.sokuteiBi = DateTime.now().toString("yyyy/MM/dd");
	}

	/** 測定日の取得 */
	public String getSokuteiBi() {
		return sokuteiBi;
	}
	/**
	 * 測定日の設定
	 * @param sokuteiBi
	 */
	public void setSokuteiBi(String sokuteiBi) {
		this.sokuteiBi = sokuteiBi;
	}

	/** 測定するバイタルリストの取得.
	 * デフォルトでは fetch=FetchType.LAZY
	 */
	@OneToMany(cascade=CascadeType.ALL, mappedBy="vital")
	public List<Vital> getVitals() {
		return vitals;
	}
	/**
	 * 測定するバイタルリストの設定.
	 * @param vitals
	 */
	public void setVitals(List<Vital> vitals) {
		this.vitals = vitals;
	}

	//id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	//バージョン
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

	//メモ
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
		Vod other = (Vod) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vod [id=" + id + ", version=" + version + ", sokuteiBi="
				+ sokuteiBi +  ", memo=" + memo + "]";
	}

}
