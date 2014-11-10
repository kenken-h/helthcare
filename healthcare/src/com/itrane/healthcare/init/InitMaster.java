package com.itrane.healthcare.init;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.service.VodService;

/**
 * バイタルマスターと一月分のバイタルデータを作成するクラス. 
 * TODO: デモ版のため、ここで1ユーザー分の作成。 
 * 実アプリではマスター保守機能を追加する必要がある
 */
public class InitMaster {

	@Autowired
	private VitalMstRepository repo;
	
	@Autowired
	private VodService vodService;

	@PostConstruct
	public void initData() {
		// ここでマスターにデータを設定する
		// TODO: デモ版の仕様。　実際には保守でマスターは作成および更新される。
		List<VitalMst> vms = repo.findAll();
		if (vms.size() == 0) {
			vms.add(new VitalMst("睡眠", "06:00", 6, 9, "睡眠", 1));
			vms.add(new VitalMst("体重", "06:00", 58, 62, "体重", 2));
			vms.add(new VitalMst("空腹時血糖", "06:00", 100, 130, "血糖", 3));
			vms.add(new VitalMst("朝体温", "06:00", 35.5, 36.9, "体温", 4));
			vms.add(new VitalMst("午前体温", "10:00", 35.5, 36.9, "体温", 5));
			vms.add(new VitalMst("午前血圧上", "10:00", 115, 140, "血圧", 6));
			vms.add(new VitalMst("午前血圧下", "10:00", 60, 90, "血圧", 7));
			vms.add(new VitalMst("朝食後血糖", "10:30", 115, 136, "血糖", 8));
			vms.add(new VitalMst("午後体温", "14:00", 35.5, 36.9, "体温", 9));
			vms.add(new VitalMst("午後血圧上", "14:00", 115, 140, "血圧", 10));
			vms.add(new VitalMst("午後血圧下", "14:00", 60, 90, "血圧", 11));
			vms.add(new VitalMst("昼食後血糖", "14:30", 115, 136, "血糖", 12));
			vms.add(new VitalMst("夕食後血糖", "20:30", 115, 136, "血糖", 13));
		}
		for (VitalMst vm : vms) {
			repo.save(vm);
		}
	}	
}
