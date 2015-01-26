package com.itrane.healthcare.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 薬服用を通知するタスク.
 */
public class TakeMedTask {
	
	static final private Logger log = LoggerFactory.getLogger(TakeMedTask.class);
	
	//@Scheduled(cron="0 */10 4-21 * * ?")
    //@Scheduled(fixedRate=5000)
    public void noticeTakeMedicine() {
		/* TODO
		 * 処方箋データを調べて服用時間を過ぎていて、服用済になってない場合に
		 * メッセージと薬の種類、量を通知する。
		 * 処方箋情報：服用時間（起床時、朝食後など）と薬の種類、量を登録.
		 * 服用時間：実際の時間ではなく通知のための目安
		 */
    	log.debug("朝食後の薬の時間です・・・");
    }
}