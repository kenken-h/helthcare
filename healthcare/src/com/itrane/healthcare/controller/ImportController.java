package com.itrane.healthcare.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.itrane.healthcare.repo.VitalMstRepository;
import com.itrane.healthcare.repo.VodRepository;
import com.itrane.healthcare.service.VodService;

/**
 * インポート処理のためのコントローラ.
 */
@Controller
public class ImportController {

	final static private Logger log = LoggerFactory.getLogger(ImportController.class);

	@Autowired
	private VodRepository vodRepo;
	@Autowired
	private VodService vodService;
	@Autowired
	private VitalMstRepository vmRepo;

	/**
	 *  インポートファイルの選択ビューを表示。
	 * @return ビュー
	 */
	@RequestMapping(value = "/importForm", method = RequestMethod.GET)
	public String importForm() {
		log.debug("");
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
		//ファイルタイプとファイル名
		log.debug("ファイルタイプ=" + mlf.getContentType() + " ファイル名=" + fileName);
		String rtn = "アップロード成功：ファイル=" + fileName;
		if (!mlf.isEmpty()) {
			try {
				byte[] bytes = mlf.getBytes();
				importExcelData(mlf, bytes);
			} catch (Exception e) {
				rtn = "アップロード失敗： " + fileName + " => " + e.getMessage();
			}
		} else {
			rtn = "アップロード失敗（ファイルが空）： " + fileName;
		}
		return toJson(rtn);
	}

	private String toJson(Object dt){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(dt);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void importExcelData(MultipartFile file, byte[] bytes) {

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
			log.debug("シート数=" + workbook.getNumberOfSheets());
			// TODO: 実際のアプリではファイル構造をチェック。（２）シート名 == "バイタル"
			log.debug("シート名=" + workbook.getSheetName(0));
			Sheet sheet = workbook.getSheetAt(0);
			log.debug("行数=" + sheet.getPhysicalNumberOfRows());
			
			List<VitalMst> vms = vmRepo.findAll();
			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					StringBuilder sb = new StringBuilder("行:" + row.getRowNum() + " : ");
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						sb.append(cell.getStringCellValue() + ", ");
					}
					log.debug(sb.toString());

					/* TODO 実際のアプリではコメントをはずす。
					//先頭列の日付に等しい Vod を検索
					List<Vod> vods = findVod(cellIterator);
					if (vods!=null) {
						if (vods.size()==0) {
							//Vod が未登録なら新規作成
							Vod vod = createVitals(cellIterator, vms);
							vodService.create(vod);
						} else { 
							//Vod が登録済みなら更新 
							Vod vod = updateVitals(vods.get(0), cellIterator, vms); 
							vodService.update(vod); 
						}
					}
					*/
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Vod updateVitals(Vod vod, Iterator<Cell> cellIterator,
			List<VitalMst> vms) {
		// TODO cellIterator の先頭列の日付に等しい Vod のバイタルデータをそれぞれの列の値で更新する
		return null;
	}

	private Vod createVitals(Iterator<Cell> cellIterator, List<VitalMst> vms) {
		// TODO cellIterator の先頭列の日付に等しい Vod を新規作成
		return null;
	}

	private List<Vod> findVod(Iterator<Cell> cellIterator) {
		// TODO cellIterator の先頭列の日付に等しい vod を検索
		return null;
	}
}
