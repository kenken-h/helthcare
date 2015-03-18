package com.itrane.healthcare.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.itrane.common.util.WebAppUtil;
import com.itrane.healthcare.repo.UserRepository;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodService;

/**
 * インポート処理のためのコントローラ.
 */
@Controller
public class ImportController {

	//final static private Logger log = LoggerFactory.getLogger(ImportController.class);

	@Autowired private VodRepository vodRepo;
	@Autowired private VodService vodService;
	@Autowired private VitalMstRepository vmRepo;
	@Autowired private UserRepository userRepo;

	/**
	 *  インポートファイルの選択ビューを表示。
	 * @return ビュー
	 */
	@RequestMapping(value = "/importForm", method = RequestMethod.GET)
	public String importForm() {
		return "/import/importForm";
	}

	/**
	 * エクセルファイルのインポート
	 * @param request
	 * @param response
	 * @return 実行結果の判定メッセージ
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public @ResponseBody String upload(MultipartHttpServletRequest request,
			HttpServletResponse response) {
		Iterator<String> itrator = request.getFileNames();
		MultipartFile mlf = request.getFile(itrator.next());
		String fileName = mlf.getOriginalFilename();
		String userName = request.getUserPrincipal().getName();
		//ファイルタイプとファイル名
		String rtn = "アップロード成功：ファイル=" + fileName;
		if (!mlf.isEmpty()) {
			try {
				byte[] bytes = mlf.getBytes();
				importExcelData(userName, mlf, bytes);
			} catch (Exception e) {
				rtn = "アップロード失敗： " + fileName + " => " + e.getMessage();
			}
		} else {
			rtn = "アップロード失敗（ファイルが空）： " + fileName;
		}
		return WebAppUtil.toJson(rtn);
	}

	/*
	 * インポートファイルが正しい列を持っているか VitalMst と付き合わせる.
	 */
	private void importExcelData(String userName, MultipartFile file, byte[] bytes) {

		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		Workbook workbook;
		try {
			if (file.getOriginalFilename().endsWith("xls")) {
				workbook = new HSSFWorkbook(bis);
			} else if (file.getOriginalFilename().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(bis);
			} else {
				throw new IllegalArgumentException(
						"標準的な excell ファイルの拡張子ではありません！");
			}
			// TODO: 実際のアプリではファイル構造をチェック。（１）シート数 == 1
			// TODO: 実際のアプリではファイル構造をチェック。（２）シート名 == "バイタル"
			Sheet sheet = workbook.getSheetAt(0);
			
			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					StringBuilder sb = new StringBuilder("行:" + row.getRowNum() + " : ");
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						sb.append(cell.getStringCellValue() + ", ");
					}

					//TODO 実際のアプリでは Vod に保存
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
