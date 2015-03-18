package com.itrane.healthcare.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.itrane.healthcare.command.VodListCmd;
import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodService;

/**
 * バイタルに関する処理をコントロールする.
 */
@Controller
public class VodController {
	
	//final static private Logger log = LoggerFactory.getLogger(VodController.class);
	
	final static private int DISP_PERIOD = 7;
	final static private String DATE_FORMAT = "yyyy/MM/dd";
	final static private String NAVIBTN_ENABLE = "可";
	final static private String NAVIBTN_DISABLE = "不可";
	final static private String CMD_KEY = "vodListCmd";
	
	@Autowired private VodRepository vodRepo;
	@Autowired private VodService vodService;	
	@Autowired private VitalMstRepository vmRepo;
	@Autowired private UserRepository userRepo;
	
	//バイタルマスター をリクエスト時に Modelにセット
	@ModelAttribute("vms")
	private List<VitalMst> getVitalMst(WebRequest request) {
		String userName = request.getUserPrincipal().getName();
		List<VitalMst> vms = new ArrayList<VitalMst>();
		UserInfo user = getUser(userName);
		if (user != null) {
			vms = vmRepo.findAllByUser(user);
		}
		return vms;
	}
	
	private UserInfo getUser(String userName) {
		List<UserInfo> pts = userRepo.findByName(userName);
		if (pts.size()==1) {
			return pts.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 過去一週間のバイタル一覧を表示する.
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/vodList", method = RequestMethod.GET)
	public String getVodList(Model model, WebRequest request) {
		DateTime dtEnd = DateTime.now();
		DateTime dtStart = dtEnd.minusDays(DISP_PERIOD-1);
		//ログインユーザー名を取得
		String userName = request.getUserPrincipal().getName();
		//ログインユーザーを取得
		UserInfo user = getUser(userName);
		List<VitalMst> vms = new ArrayList<VitalMst>();
		List<Vod> vods = new ArrayList<Vod>();
		String prev = NAVIBTN_DISABLE;
		if (user != null) {
			vms = vmRepo.findAllByUser(user);
			vods = vodService.findBySokuteiBiBetween(user,
				dtStart.toString(DATE_FORMAT), dtEnd.toString(DATE_FORMAT));
			prev = checkPrev(user, dtStart, vods);
		}
		String next = NAVIBTN_DISABLE;
		VodListCmd cmd =  new VodListCmd(prev, next, vms, vods, dtStart);
		request.setAttribute(CMD_KEY, cmd, RequestAttributes.SCOPE_SESSION);
		model.addAttribute(CMD_KEY, cmd);
		return "/vod/vodList";
	}
	
	@RequestMapping(value = "/prevWeek", method = RequestMethod.GET)
	public String prevWeek(Model model, WebRequest request) {
		VodListCmd cmd = (VodListCmd)request.getAttribute(CMD_KEY,
				RequestAttributes.SCOPE_SESSION);
		String userName = request.getUserPrincipal().getName();
		UserInfo user = getUser(userName);
		if (cmd.getPrev().equals(NAVIBTN_ENABLE) && user!=null) {
			DateTime dtStart = cmd.getStartDt();
			DateTime dtPrevStart = dtStart.minusDays(DISP_PERIOD);
			DateTime dtPrevEnd = dtPrevStart.plusDays(DISP_PERIOD-1);
			List<Vod> vods = vodService.findBySokuteiBiBetween(user,
					dtPrevStart.toString(DATE_FORMAT), dtPrevEnd.toString(DATE_FORMAT));
			cmd.setVods(vods);
			cmd.setPrev(checkPrev(user, dtPrevStart, vods));
			cmd.setNext(NAVIBTN_ENABLE);
			cmd.setStartDt(dtPrevStart);
		}
		model.addAttribute(CMD_KEY, cmd);
		return "/vod/vodList";
	}
	
	@RequestMapping(value = "/nextWeek", method = RequestMethod.GET)
	public String nextWeek(Model model, WebRequest request) {
		VodListCmd cmd = (VodListCmd)request.getAttribute(CMD_KEY,
				RequestAttributes.SCOPE_SESSION);
		String userName = request.getUserPrincipal().getName();
		if (cmd.getNext().equals(NAVIBTN_ENABLE)) {
			DateTime dtStart = cmd.getStartDt();
			DateTime dtNextStart = dtStart.plusDays(DISP_PERIOD);
			DateTime dtNextEnd = dtNextStart.plusDays(DISP_PERIOD-1);
			List<Vod> vods = vodService.findBySokuteiBiBetween(userName,
					dtNextStart.toString(DATE_FORMAT), dtNextEnd.toString(DATE_FORMAT));
			cmd.setVods(vods);
			cmd.setPrev(NAVIBTN_ENABLE);
			cmd.setNext(checkNext(dtNextStart));
			cmd.setStartDt(dtNextStart);
		}
		model.addAttribute(CMD_KEY, cmd);
		return "/vod/vodList";
	}

	// 前週を表示可能か判定する
	private String checkPrev(UserInfo user, DateTime dtPrevStart, List<Vod> vods) {
		if (vods.size() < DISP_PERIOD) {
			return NAVIBTN_DISABLE;
		} else {
			List<Vod> prevVods = vodRepo.findBySokuteiBi(user,
					dtPrevStart.minusDays(1).toString(DATE_FORMAT));
			if (prevVods.size()==0) {
				return NAVIBTN_DISABLE;
			}
		}
		return NAVIBTN_ENABLE;
	}
	// 次週を表示可能か判定する
	private String checkNext(DateTime startDt) {
		if (startDt.plusDays(8).isAfter(DateTime.now())) {
			return NAVIBTN_DISABLE;
		}
		return NAVIBTN_ENABLE;
	}

	//===========
	// チャート
	//===========
	/**
	 * 血糖値のチャート.
	 * @return　チャートデータ
	 */
	@RequestMapping(value = "/showBsChart", method = RequestMethod.POST)
	@ResponseBody
	public Object[][] showBsChart(WebRequest request) {
		List<Vod> vods = getVods(request);
		String[] headers = {"日付","空腹時","朝食後","昼食後","夕食後","平均値"};
		Object[][] result = getChartData(headers, vods, "血糖");
		return result;
	}

	/**
	 * 体温のチャート.
	 * @return　チャートデータ
	 */
	@RequestMapping(value = "/showBtChart", method = RequestMethod.POST)
	@ResponseBody
	public Object[][] showBtChart(WebRequest request) {
		List<Vod> vods = getVods(request);
		String[] headers = {"日付","朝","午前","午後"};
		Object[][] result = getChartData(headers, vods, "体温");
		return result;
	}

	/**
	 * 血圧のチャート.
	 * @return　チャートデータ
	 */
	@RequestMapping(value = "/showBpChart", method = RequestMethod.POST)
	@ResponseBody
	public Object[][] showBpChart(WebRequest request) {
		List<Vod> vods = getVods(request);
		String[] headers = {"日付","午前上","午前下","午後上","午後下"};
		Object[][] result = getChartData(headers, vods, "血圧");
		return result;
	}

	/**
	 * 体重のチャート.
	 * @return　チャートデータ
	 */
	@RequestMapping(value = "/showWtChart", method = RequestMethod.POST)
	@ResponseBody
	public Object[][] showWtChart(WebRequest request) {
		List<Vod> vods = getVods(request);
		String[] headers = {"日付","体重"};
		Object[][] result = getChartData(headers, vods, "体重");
		return result;
	}

	/*
	 * カレントの週のバイタルデータを取得する.
	 */
	private List<Vod> getVods(WebRequest request) {
		VodListCmd cmd = (VodListCmd)request.getAttribute(CMD_KEY,
				RequestAttributes.SCOPE_SESSION);
		String userName = request.getUserPrincipal().getName();
		DateTime dtStart = cmd.getStartDt();
		DateTime dtEnd = dtStart.plusDays(DISP_PERIOD-1);
		List<Vod> vods = vodService.findBySokuteiBiBetween(userName,
				dtStart.toString(DATE_FORMAT), dtEnd.toString(DATE_FORMAT));
		return vods;
	}

	/*
	 * 指定されたヘッダとバイタルデータから
	 * 指定タイプのチャートデータを作成する.
	 */
	private Object[][] getChartData(String[] headers, List<Vod> vods,
			String type) {
		int row=0, col=0;
		Object[][] result = new Object[vods.size()+1][headers.length];
		for (String header: headers) {
			result[row][col++] = header;
		}
		col=0;
		row++;
		for (Vod vod: vods) {
			result[row][col++] = vod.getSokuteiBi();
			double sum = 0;
			for(Vital vt: vod.getVitals()) {
				if (vt.getVitalM().getType().equals(type)) {
					result[row][col++] = new Double(vt.getSokuteiTi());
					if (type.equals("血糖")) {
						sum = sum + Double.parseDouble(vt.getSokuteiTi());
					}
				}
			}
			if (type.equals("血糖")) {
				result[row][col++] = new Double(sum/4);
			}
			row++;
			col=0;
		}
		return result;
	}
}
