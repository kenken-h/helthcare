package com.itrane.healthcare.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.itrane.healthcare.command.HomeCmd;
import com.itrane.healthcare.command.VitalCmd;
import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodService;

/**
 * 起動時のホーム画面表示のリクエストに対するコントローラ. TODO: コマンド版に変更（エラー処理も）
 */
@Controller
public class HomeController {

	//final static private Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired private VodRepository vodRepo;
	@Autowired private VodService vodService;
	@Autowired private VitalMstRepository vmRepo;
	@Autowired private UserRepository userRepo;

	// HomeCmd をリクエスト時に Modelにセット
	@ModelAttribute("homeCmd")
	private HomeCmd getHomeCmd(WebRequest request) {
		Vod vod = null;
		List<VitalMst> vms = new ArrayList<VitalMst>();
		//ログインユーザー名を取得
		String userName = request.getUserPrincipal().getName();
		//ログインユーザーを取得
		List<UserInfo> pts = userRepo.findByName(userName);
		List<VitalCmd> vcmds = null;
		if (pts.size() == 1) {
			UserInfo user = pts.get(0);
			//ログインユーザーのバイタルマスターを取得
			vms = vmRepo.findAllByUser(user);
			//当日の Vod を取得
			List<Vod> vods = vodService.findBySokuteiBi(user,
					DateTime.now().toString("yyyy/MM/dd"));
			if (vods.size() == 0) {
				//当日の Vod が無ければ空の Vod を作成
				vod = new Vod(user, DateTime.now().toString("yyyy/MM/dd"));
				List<Vital> vitals = new ArrayList<Vital>();
				vcmds = createVcmds(vms, vitals, vod);
				vod.setVitals(vitals);
				vodService.create(vod);
			} else {
				vod = vods.get(0);
				vcmds = createVcmds(vms, null, null);
			}
		} 
		HomeCmd cmd = new HomeCmd(vod, vcmds, vod.getSokuteiBi(), vod.getMemo());
		return cmd;
	}

	private List<VitalCmd> createVcmds(List<VitalMst> vms, List<Vital> vitals, Vod vod) {
		List<VitalCmd> vcmds = new ArrayList<VitalCmd>();
		if (vms.size() > 0) {
			for (VitalMst vm : vms) {
				if (vitals!=null) {
					vitals.add(new Vital(vm.getName(), "", "0", vod, vm));
				}
				vcmds.add(new VitalCmd(vm.getName(), "", "0",
						vm.getJikan(), vm.getType(), "", vm.getKijunMin()
								.doubleValue(), vm.getKijunMax()
								.doubleValue()));
			}
		}
		return vcmds;
	}

	/**
	 * 起動時または updateVod GETリクエストに応答. 実行前に @ModelAttribute("homeCmd")により model
	 * にセットされる
	 * @param principal
	 * @return レスポンスビュー (/views/home/home.html)
	 */
	@RequestMapping(value = { "/", "/updateVod" }, method = RequestMethod.GET)
	public String editVod(Principal principal) {
		return "/home/home";
	}

	/**
	 * updateVod POSTリクエストに応答、 vod を更新する
	 * @param homeCmd
	 * @param inputCmd
	 * @param model
	 * @param result
	 * @return
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
