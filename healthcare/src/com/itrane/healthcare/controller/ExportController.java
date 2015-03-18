package com.itrane.healthcare.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodService;

/**
 * エクスポーt処理のためのコントローラ.
 */
@Controller
public class ExportController {

	//final static private Logger log = LoggerFactory.getLogger(ExportController.class);

	@Autowired private VodRepository vodRepo;
	@Autowired private VodService vodService;
	@Autowired private VitalMstRepository vmRepo;
	@Autowired private UserRepository userRepo;

	@RequestMapping(value = "/exportForm", method = RequestMethod.GET)
	public String exportForm() {
		return "/export/exportForm";
	}

	@RequestMapping(value = "downloadExcel", method = RequestMethod.GET)
	public ModelAndView downloadExcel(HttpServletRequest rq) {
		String userName = rq.getUserPrincipal().getName();
		ModelAndView mav = createModelAndView(userName, "excelView");
		mav.addObject("fileName", "vod-" + userName + "-" + 
				DateTime.now().toString("yyyyMMdd-hhmmss") + ".xls");
		return mav;
	}

	@RequestMapping(value = "downloadPdf", method = RequestMethod.GET)
	public ModelAndView downloadPdf(WebRequest request) {
		String userName = request.getUserPrincipal().getName();
		return createModelAndView(userName, "pdfView");
	}

	private ModelAndView createModelAndView(String userName, String viewName) {
		ModelAndView mav = new ModelAndView(viewName);
		List<VitalMst> vms = new ArrayList<VitalMst>();
		List<Vod> vods = new ArrayList<Vod>();
		List<UserInfo> pts = userRepo.findByName(userName);
		if (pts.size() == 1) {
			UserInfo user = pts.get(0);
			vms = vmRepo.findAllByUser(user);
			vods = vodRepo.findAllByUser(user);
		}
		mav.addObject("vods", vods);
		mav.addObject("vms", vms);
		return mav;
	}
}
