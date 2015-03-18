package com.itrane.healthcare.init;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.itrane.healthcare.model.UserInfo;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalMstRepository;

/**
 * バイタルマスターと一月分のバイタルデータを作成するクラス. 
 * TODO: デモ版のため、ここで1ユーザー分の作成。 
 * 実アプリではマスター保守機能を追加する必要がある
 */
public class InitMaster {

	@Autowired private VitalMstRepository vmRepo;
	@Autowired private UserRepository userRepo;
	
	public InitMaster() {
		// TODO Auto-generated constructor stub
	}

	public InitMaster(VitalMstRepository repo) {
		this.vmRepo = repo;
	}

	@PostConstruct
	@Transactional
	public void initData() {
		// ここでマスターにデータを設定する
		// TODO: デモ版の仕様。　実際には保守でマスターは作成および更新される。
		
		//ユーザーマスター, バイタルマスターの作成
		UserInfo user = userRepo.save(new UserInfo("user1", "user1", "太田", "佳恵", "ROLE_ADMIN"));
		vmRepo.save(new VitalMst("睡眠", "06:00", 6, 9, "睡眠", 1, user));
		vmRepo.save(new VitalMst("体重", "06:00", 58, 62, "体重", 2, user));
		vmRepo.save(new VitalMst("空腹時血糖", "06:00", 100, 130, "血糖", 3, user));
		vmRepo.save(new VitalMst("朝体温", "06:00", 35.5, 36.9, "体温", 4, user));
		vmRepo.save(new VitalMst("午前体温", "10:00", 35.5, 36.9, "体温", 5, user));
		vmRepo.save(new VitalMst("午前血圧上", "10:00", 115, 140, "血圧", 6, user));
		vmRepo.save(new VitalMst("午前血圧下", "10:00", 60, 90, "血圧", 7, user));
		vmRepo.save(new VitalMst("朝食後血糖", "10:30", 115, 136, "血糖", 8, user));
		vmRepo.save(new VitalMst("午後体温", "14:00", 35.5, 36.9, "体温", 9, user));
		vmRepo.save(new VitalMst("午後血圧上", "14:00", 115, 140, "血圧", 10, user));
		vmRepo.save(new VitalMst("午後血圧下", "14:00", 60, 90, "血圧", 11, user));
		vmRepo.save(new VitalMst("昼食後血糖", "14:30", 115, 136, "血糖", 12, user));
		vmRepo.save(new VitalMst("夕食後血糖", "20:30", 115, 136, "血糖", 13, user));

		user = userRepo.save(new UserInfo("user2", "user2", "山本", "一郎", "ROLE_USER"));
		vmRepo.save(new VitalMst("睡眠", "06:00", 6, 9, "睡眠", 1, user));
		vmRepo.save(new VitalMst("体重", "06:00", 58, 62, "体重", 2, user));
		vmRepo.save(new VitalMst("朝体温", "06:00", 35.5, 36.9, "体温", 4, user));
		vmRepo.save(new VitalMst("午前体温", "10:00", 35.5, 36.9, "体温", 5, user));
		vmRepo.save(new VitalMst("午前血圧上", "10:00", 115, 140, "血圧", 6, user));
		vmRepo.save(new VitalMst("午前血圧下", "10:00", 60, 90, "血圧", 7, user));
		vmRepo.save(new VitalMst("午後体温", "14:00", 35.5, 36.9, "体温", 9, user));
		vmRepo.save(new VitalMst("午後血圧上", "14:00", 115, 140, "血圧", 10, user));
		vmRepo.save(new VitalMst("午後血圧下", "14:00", 60, 90, "血圧", 11, user));

		user = userRepo.save(new UserInfo("user3", "user3", "岡田", "和夫", "ROLE_USER"));
		vmRepo.save(new VitalMst("睡眠", "06:00", 6, 9, "睡眠", 1, user));
		vmRepo.save(new VitalMst("体重", "06:00", 58, 62, "体重", 2, user));
		vmRepo.save(new VitalMst("午前体温", "10:00", 35.5, 36.9, "体温", 5, user));
		vmRepo.save(new VitalMst("午前血圧上", "10:00", 115, 140, "血圧", 6, user));
		vmRepo.save(new VitalMst("午前血圧下", "10:00", 60, 90, "血圧", 7, user));
		vmRepo.save(new VitalMst("午後体温", "14:00", 35.5, 36.9, "体温", 9, user));
		vmRepo.save(new VitalMst("午後血圧上", "14:00", 115, 140, "血圧", 10, user));
		vmRepo.save(new VitalMst("午後血圧下", "14:00", 60, 90, "血圧", 11, user));

		user = userRepo.save(new UserInfo("user4", "user4", "松井", "秀子", "ROLE_USER"));
		user = userRepo.save(new UserInfo("user5", "user5", "倉田", "花子", "ROLE_ADMIN,ROLE_USER"));
	}	
}
