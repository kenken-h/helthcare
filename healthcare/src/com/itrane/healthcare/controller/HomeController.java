package com.itrane.healthcare.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itrane.healthcare.command.HomeCmd;
import com.itrane.healthcare.command.VitalCmd;
import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodNotFound;
import com.itrane.healthcare.service.VodService;

/**
 * 起動時のホーム画面表示のリクエストに対するコントローラ. TODO: コマンド版に変更（エラー処理も）
 */
@Controller
public class HomeController {

	final static private Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private VodRepository vodRepo;
	@Autowired
	private VodService vodService;
	@Autowired
	private VitalMstRepository vmRepo;

	// HomeCmd をリクエスト時に Modelにセット
	@ModelAttribute("homeCmd")
	private HomeCmd getHomeCmd() {
		Vod vod;
		List<VitalMst> vms;
		List<Vod> vods = vodService.findBySokuteiBi(DateTime.now().toString(
				"yyyy/MM/dd"));
		List<VitalCmd> vcmds = null;
		if (vods.size() == 0) {
			vod = new Vod();
			vms = vmRepo.findAll();
			List<Vital> vitals = new ArrayList<Vital>();
			vcmds = new ArrayList<VitalCmd>();
			if (vms.size() > 0) {
				for (VitalMst vm : vms) {
					vitals.add(new Vital(vm.getName(), "", "0", vod, vm));
					vcmds.add(new VitalCmd(vm.getName(), "", "0",
							vm.getJikan(), vm.getType(), "", vm.getKijunMin()
									.doubleValue(), vm.getKijunMax()
									.doubleValue()));
				}
			}
			vod.setVitals(vitals);
			vodService.create(vod);
		} else {
			vod = vods.get(0);
			vms = vmRepo.findAll();
			vcmds = new ArrayList<VitalCmd>();
			if (vms.size() > 0) {
				for (VitalMst vm : vms) {
					vcmds.add(new VitalCmd(vm.getName(), "", "0",
							vm.getJikan(), vm.getType(), "", vm.getKijunMin()
									.doubleValue(), vm.getKijunMax()
									.doubleValue()));
				}
			}
		}
		HomeCmd cmd = new HomeCmd(vod, vcmds, vod.getSokuteiBi(), vod.getMemo());
		return cmd;
	}

	/**
	 * 起動時または updateVod GETリクエストに応答. 実行前に @ModelAttribute("homeCmd")により model
	 * にセットされる
	 * 
	 * @return レスポンスビュー (/views/home/home.html)
	 */
	@RequestMapping(value = { "/", "/updateVod" }, method = RequestMethod.GET)
	public String editVod() {
		return "/home/home";
	}

	/**
	 * updateVod POSTリクエストに応答、 vod を更新する
	 * 
	 * @param vod
	 *            　システム日の Vod
	 * @return レスポンスビュー (/views/home/home.html)
	 * @throws VodNotFound
	 */
	@RequestMapping(value = "/updateVod", method = RequestMethod.POST)
	public String saveVod(@ModelAttribute(value = "homeCmd") HomeCmd homeCmd,
			final @Valid HomeCmd inputCmd, Model model, BindingResult result) {
		homeCmd.checkErrorsAndUpdateVod(inputCmd);
		vodService.update(homeCmd.getVod());
		model.addAttribute("homeComd", inputCmd);
		return "/home/home";
	}
}
