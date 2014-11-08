package com.itrane.healthcare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 起動時のホーム画面表示のリクエストに対するコントローラ.
 */
@Controller
public class HomeController {

	final static private Logger log = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * 起動時 GETリクエストに応答.
	 * @return レスポンスビュー (/views/home/home.html)
	 */
	@RequestMapping(value={"/"}, method = RequestMethod.GET)
	public String editVod() {
		return "/home/home";
	}
	
}
