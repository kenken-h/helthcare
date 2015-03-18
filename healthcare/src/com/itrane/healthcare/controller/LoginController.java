package com.itrane.healthcare.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	/**
	 * ログインフォームを表示する。
	 * @param rq
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest rq) {
		return "login";
	}

	/**
	 * ログアウト処理を実行。ログインフォームを表示。
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout();
		return "/logout";
	}

	/**
	 * アクセスエラーページを表示。
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/error")
	public String error(HttpServletRequest request, Model model) {
		return "error";
	}
}