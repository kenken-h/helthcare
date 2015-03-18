package com.itrane.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrane.common.model.DtAjaxData;

public class WebAppUtil {

	/**
	 * オブジェクトを JSON 文字列に変換.
	 * 
	 * @param dt
	 * @return
	 */
	static public String toJson(Object dt) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(dt);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * JSON文字列から指定タイプのインスタンスを取得.
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	static public <T> T fromJson(String json, Class<T> type) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		T t = null;
		try {
			t = mapper.readValue(json, type);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * dataTables ページ情報のクエリー文字列を DtAjaxData に変換する.
	 * @param qs クエリー文字列
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	static public DtAjaxData getDtAjaxData(String qs) throws UnsupportedEncodingException {
		DtAjaxData dta = new DtAjaxData();
		if (qs!=null && qs.length() > 0) {
			String[] ps = URLDecoder.decode(qs, "UTF-8").split("&");
			if (ps.length > 0) {
				String json = ps[0];
				dta = fromJson(json, DtAjaxData.class);
			}
		}
		return dta;
		
	}
}
