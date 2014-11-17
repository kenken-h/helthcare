package com.itrane.healthcare.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodService;

/**
 * バイタルに関する処理をコントロールする.
 */
@Controller
public class VodController {
	
	final static private Logger log = LoggerFactory.getLogger(VodController.class);
	
	final static private int DISP_PERIOD = 7;
	final static private String DATE_FORMAT = "yyyy/MM/dd";
	final static private String NAVIBTN_ENABLE = "可";
	final static private String NAVIBTN_DISABLE = "不可";
	final static private String CMD_KEY = "vodListCmd";
	
	@Autowired
	private VodRepository vodRepo;
	@Autowired
	private VodService vodService;	
	@Autowired
	private VitalMstRepository vmRepo;
	
	//バイタルマスター をリクエスト時に Modelにセット
	@ModelAttribute("vms")
	private List<VitalMst> getVitalMst() {
		List<VitalMst> vms = vmRepo.findAll();
		return vms;
	}
	
	/**
	 * 過去一週間のバイタル一覧を表示する.
	 * 
	 * @return
	 */
	@RequestMapping(value="/vodList", method = RequestMethod.GET)
	public String getVodList(Model model, WebRequest request) {
		DateTime dtEnd = DateTime.now();
		DateTime dtStart = dtEnd.minusDays(DISP_PERIOD-1);

		List<Vod> vods = vodService.findBySokuteiBiBetween(
				dtStart.toString(DATE_FORMAT), dtEnd.toString(DATE_FORMAT));
		List<VitalMst> vms = vmRepo.findAll();
		String prev = checkPrev(dtStart, vods);
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
		if (cmd.getPrev().equals(NAVIBTN_ENABLE)) {
			DateTime dtStart = cmd.getStartDt();
			DateTime dtPrevStart = dtStart.minusDays(DISP_PERIOD);
			DateTime dtPrevEnd = dtPrevStart.plusDays(DISP_PERIOD-1);
			List<Vod> vods = vodService.findBySokuteiBiBetween(
					dtPrevStart.toString(DATE_FORMAT), dtPrevEnd.toString(DATE_FORMAT));
			cmd.setVods(vods);
			cmd.setPrev(checkPrev(dtPrevStart, vods));
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
		if (cmd.getNext().equals(NAVIBTN_ENABLE)) {
			DateTime dtStart = cmd.getStartDt();
			DateTime dtNextStart = dtStart.plusDays(DISP_PERIOD);
			DateTime dtNextEnd = dtNextStart.plusDays(DISP_PERIOD-1);
			List<Vod> vods = vodService.findBySokuteiBiBetween(
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
	private String checkPrev(DateTime dtPrevStart, List<Vod> vods) {
		if (vods.size() < DISP_PERIOD) {
			return NAVIBTN_DISABLE;
		} else {
			List<Vod> prevVods = vodRepo.findBySokuteiBi(
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "showBsChart", method = RequestMethod.POST)
	@ResponseBody
	public Object[][] showBsChart(WebRequest request) {
		VodListCmd cmd = (VodListCmd)request.getAttribute(CMD_KEY,
				RequestAttributes.SCOPE_SESSION);
		DateTime dtStart = cmd.getStartDt();
		DateTime dtEnd = dtStart.plusDays(DISP_PERIOD-1);
		List<Vod> vods = vodService.findBySokuteiBiBetween(
				dtStart.toString(DATE_FORMAT), dtEnd.toString(DATE_FORMAT));
		int rows = vods.size() + 1; //ヘッダ分を追加
		int cols = 6;
		Object[][] result = new Object[rows][cols];
		int row=0, col=0;
		result[row][col++] = "日付";
		result[row][col++] = "空腹時";
		result[row][col++] = "朝食後";
		result[row][col++] = "昼食後";
		result[row][col++] = "夕食後";
		result[row][col++] = "平均値";
		row++;
		col = 0;
		for (Vod vod: vods) {
			result[row][col++] = vod.getSokuteiBi();
			double sum = 0;
			for(Vital vt: vod.getVitals()) {
				if (vt.getName().endsWith("血糖")) {
					result[row][col++] = new Double(vt.getSokuteiTi());
					sum = sum + Double.parseDouble(vt.getSokuteiTi());
				}
			}
			result[row][col++] = new Double(sum/4);
			row++;
			col=0;
		}
		return result;
	}
}
