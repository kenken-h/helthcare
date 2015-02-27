package com.itrane.healthcare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * ユーザー情報の処理を行うコントローラ.
 */
@Controller
public class UserController {
	
	@RequestMapping(value="/admin/userList", method=RequestMethod.GET)
	public ModelAndView userList() {
		ModelAndView mav = new ModelAndView("/user/userList");
		return mav;
	}	
}
