package com.itrane.healthcare.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itrane.common.model.DataTableObject;
import com.itrane.common.model.DtAjaxData;
import com.itrane.common.util.WebAppUtil;
import com.itrane.healthcare.command.UserCmd;
import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.service.DbAccessException;
import com.itrane.healthcare.service.UserService;

/**
 * ユーザー情報の処理を行うコントローラ.
 */
@Controller
public class UserController {
	
	//private static Logger log = LoggerFactory.getLogger(UserController.class); 
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;

	/**
	 * ユーザー一覧ビューを表示する.  (管理者権限)
	 * @return 
	 */
	@RequestMapping(value="/admin/userList", method=RequestMethod.GET)
	public ModelAndView userList() {
		ModelAndView mav = new ModelAndView("/user/userList");
		mav.addObject("cmd", new UserCmd());	//ステータス：初期状態
		mav.addObject("user", new UserInfo());
		return mav;
	}
	
	/**
	 * 取得ページ情報に基づいて DataTable を更新する. 
	 * @param request
	 * @return DataTableObject の JSON文字列
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/admin/userPage", produces="text/html;charset=UTF-8")
	public @ResponseBody String getUserPage(HttpServletRequest request) 
			throws UnsupportedEncodingException {
		String qs = request.getQueryString();
		List<UserInfo> users = null;
		DtAjaxData dta = WebAppUtil.getDtAjaxData(qs);
		long total = userRepo.count();
		users = userService.findPage(dta.start, dta.length, dta.search.value,
				dta.getSortDir(), dta.getSortCol() ); 
		DataTableObject<UserInfo> dt = new DataTableObject<UserInfo>(
				users, dta.draw, total, (int)total);
		return WebAppUtil.toJson(dt);
	}

	/**
	 * id で指定した患者マスターを編集.
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/admin/editUser/{id}",
			produces="text/html;charset=UTF-8", method=RequestMethod.GET)
	public @ResponseBody String editUser(@PathVariable Long id) {
		UserInfo pt = userRepo.findOne(id);
		return WebAppUtil.toJson(pt);
	}	
	
	/**
	 * ユーザー保存（ajax版).
	 * @param cmd UserCmd
	 * @return
	 */
	@RequestMapping(value="/admin/save.user")
	public @ResponseBody UserCmd save(@RequestBody UserCmd cmd) {
		UserInfo user = cmd.getUser();
        Set<ConstraintViolation<UserInfo>> violations = 
        		Validation.buildDefaultValidatorFactory().getValidator().validate(user);
		Map<String, String> errMap = new HashMap<String, String>();
		if(violations.size()>0){
			cmd.setStatus(9);
			Iterator<ConstraintViolation<UserInfo>> itr = violations.iterator();
			while(itr.hasNext()) {
				ConstraintViolation<UserInfo> cs = itr.next();
				errMap.put(cs.getPropertyPath().toString(), cs.getMessage());
			}
		} else {
			try {
				userService.save(user);
				cmd.setStatus(1);
			} catch (DbAccessException dex) {
				cmd.setStatus(9);
				if (dex.getMessage().startsWith("楽観的")) {
					//TODO 楽観的ロックエラーの処理
				} else if (dex.getMessage().startsWith("重複")) {
					errMap.put("name", "このユーザー名はすでに登録されています");
					cmd.setFldErrors(errMap);
					return cmd;
				}
			}
		}
		cmd.setFldErrors(errMap);
		return cmd;
	}
}
