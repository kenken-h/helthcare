package com.itrane.common.model;

import java.util.List;

public class DtAjaxData {
	public int draw;
	public List<DtColumns> columns;
	public List<DtOrder> order;
	public int start;
	public int length;
	public DtSearch search;
	

	public String getSortCol() {
		if (order.size()>0) {
			return columns.get(order.get(0).column).data;
		}
		return "";
	}
	
	public String getSortDir() {
		if (order.size()>0) {
			return order.get(0).dir;
		}
		return "";
	}
	
	@Override
	public String toString() {
		return "DtAjaxData [draw=" + draw + ", columns=" + columns + ", order="
				+ order + ", start=" + start + ", length=" + length
				+ ", search=" + search + "]";
	}
}
