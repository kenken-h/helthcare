package com.itrane.healthcare.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodService;

/**
 * エクスポーt処理のためのコントローラ.
 */
@Controller
public class ExportController {

	final static private Logger log = LoggerFactory.getLogger(ExportController.class);

	@Autowired
	private VodRepository vodRepo;
	@Autowired
	private VodService vodService;
	@Autowired
	private VitalMstRepository vmRepo;

	@RequestMapping(value = "/exportForm", method = RequestMethod.GET)
	public String exportForm() {
		return "/export/exportForm";
	}

	@RequestMapping(value = "downloadExcel", method = RequestMethod.GET)
	public ModelAndView downloadExcel() {
		List<VitalMst> vms = vmRepo.findAll();
		List<Vod> vods = vodRepo.findAll();
		ModelAndView mav = new ModelAndView("excelView");
		mav.addObject("vods", vods);
		mav.addObject("vms", vms);
		return mav;
	}

	@RequestMapping(value = "downloadPdf", method = RequestMethod.GET)
	public ModelAndView downloadPdf() {
		List<VitalMst> vms = vmRepo.findAll();
		List<Vod> vods = vodRepo.findAll();
		ModelAndView mav = new ModelAndView("pdfView");
		mav.addObject("vods", vods);
		mav.addObject("vms", vms);
		return mav;
	}
}
