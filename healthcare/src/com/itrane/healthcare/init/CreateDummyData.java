package com.itrane.healthcare.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.service.VodService;

/**
 * バイタルマスターと一月分のバイタルデータを作成するクラス. 
 * TODO: デモ版のため、ここで1ユーザー分の作成。 
 * 実アプリではマスター保守機能を追加する必要がある
 */
public class CreateDummyData {

	@Autowired
	private VitalMstRepository repo;
	
	@Autowired
	private VodService vodService;

	@PostConstruct
	public void createData() {
		DateTime todayDt = DateTime.now();
		DateTime startDt = todayDt.minusMonths(1);
		List<VitalMst> vms = repo.findAll();
		int c = 0;
		for (DateTime d = startDt; d.isBefore(todayDt); d = d.plusDays(1)) {
			List<Vital> vitals = new ArrayList<Vital>();
			Vod todayVod = new Vod();
			todayVod.setSokuteiBi(d.toString("yyyy/MM/dd"));
			if (vms.size() > 0) {
				for (VitalMst vm : vms) {
					if (c >= BS_HOSEI.length) {
						c = 0;
					}
					vitals.add(createVital(vm, todayVod, c));
					c++;
				}
			}
			todayVod.setVitals(vitals);
			vodService.create(todayVod);
		}
		vms = repo.findAll();
	}
	
	private double FBS = 110;
	private double BS = 140;
	private double BW = 60;
	private double BT = 36.0;
	private double BPL = 90;
	private double BPH = 135;
	private double SLP = 6;

	private double FBS_HOSEI[] = { 20,  -5,  25,  30, -10,  50,  10, 10, -20,  35, 40, -5,  25,  30}; 
	private double BS_HOSEI[] =  { 30,   0,  20,  20,  -5,  30,  70, 20,  30,  -5, 30, 30,  20,  30};
	private double BW_HOSEI[] =  {-.2, -.3, -.4, -.5, -.7, -.8, -.8,-.7, -.6, -.4,-.2, .1,  .3,  .1};
	private double BT_HOSEI[] =  { .3,  .4,  .5,  .4,  .3, -.2,  .3, .7,  .8,  .6, .5, .8, 1.2,  .6};
	private double BPL_HOSEI[] = { 12,   5,  14,   5,  10,  -5,  19, 11,   5,  13, -5, 20,  15,   5};
	private double BPH_HOSEI[] = { 19,  25,  13,  30,  20, -10,  28, 20,  25,  15, 30, 20, -10,  22};
	private double SLP_HOSEI[] = { .5,  .5,   0, -.5,   0,   0, -.5,  0,   0, -.5,  0, -1,   1, 1.5};

	private Vital createVital(VitalMst vm, Vod vod, int c) {
		double val = 0;
		if (vm.getType().equals("睡眠")) {
			val = getVal(SLP, SLP_HOSEI[c]);
		} else if (vm.getType().equals("体重")) {
			val = getVal(BW, BW_HOSEI[c]);
		} else if (vm.getType().equals("血糖")) {
			if (vm.getName().equals("空腹時血糖")) {
				val = getVal(FBS, FBS_HOSEI[c]);
			} else {
				val = getVal(BS, BS_HOSEI[c]);
			}
		} else if (vm.getType().equals("血圧")) {
			if (vm.getName().endsWith("上")) {
				val = getVal(BPH, BPH_HOSEI[c]);
			} else {
				val = getVal(BPL, BPL_HOSEI[c]);
			}
		} else if (vm.getType().equals("体温")) {
			val = getVal(BT, BT_HOSEI[c]);
		}
		return new Vital(vm.getName(), vm.getJikan(), val + "", vod, vm);
	}

	private double getVal(double kijun, double hosei) {
		return  kijun + hosei;
	}
}
