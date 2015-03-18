package com.itrane.healthcare.command;

import java.io.Serializable;
import java.util.List;

import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.Vod;

/**
 * ホームビュー（home.html）用コマンド.
 */
public class HomeCmd implements Serializable {

	private static final long serialVersionUID = 1L;

	private Vod vod;
	private List<VitalCmd> vitalCmds;
	private String sokuteiBi;
	private String memo;
	private String status;

	public HomeCmd() {}
	
	public HomeCmd(Vod vod, List<VitalCmd> vitalCmds, String sokuteiBi,
			String memo) {
		super();
		this.vod = vod;
		this.vitalCmds = vitalCmds;
		this.sokuteiBi = sokuteiBi;
		this.memo = memo;
	}

	/**
	 * home.html の入力値を検証し、Vod を更新する。
	 * 不正な入力値は Vod に反映しない。値が正しければ反映する。
	 * @param inputCmd
	 */
	public void checkErrorsAndUpdateVod(HomeCmd inputCmd) {
		vod.setMemo(inputCmd.getMemo());
		int i = 0;
		for (VitalCmd vc : inputCmd.getVitalCmds()) {
			Vital todayVital = vod.getVitals().get(i);
			VitalCmd todayCmd = vitalCmds.get(i);
			String sokuteiTi = vc.getSokuteiTi();
			//null,空文字は不可
			if (sokuteiTi!=null && !sokuteiTi.equals("")) {
				double d = 0;
				try {
					d = Double.parseDouble(vc.getSokuteiTi());
				} catch (NumberFormatException ne) {
				}
				//1以上の数値 && 未登録のデータにのみ入力値を反映する。
				if (d > 0) {
						todayVital.setSokuteiTi(vc.getSokuteiTi());
						// TODO: デモ版の仕様。実際はシステム時間を設定する。
						todayVital.setSokuteiJikan(todayVital.getVitalM().getJikan());
						todayCmd.setSokuteiJikan(todayVital.getVitalM().getJikan());
				} else if(!todayVital.getSokuteiTi().equals("0")) {
					vc.setSokuteiTi(todayVital.getSokuteiTi());
					vc.setSokuteiJikan(todayVital.getSokuteiJikan());
				}
			} else {
				//エラー
				vc.setErrMsg("入力エラー");
				vc.setSokuteiTi("0");
			}
			i++;
		}
	}
	
	public Vod getVod() {
		return vod;
	}

	public void setVod(Vod vod) {
		this.vod = vod;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSokuteiBi() {
		return sokuteiBi;
	}

	public void setSokuteiBi(String sokuteiBi) {
		this.sokuteiBi = sokuteiBi;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<VitalCmd> getVitalCmds() {
		return vitalCmds;
	}

	public void setVitalCmds(List<VitalCmd> vitalCmds) {
		this.vitalCmds = vitalCmds;
	}

}
