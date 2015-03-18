package com.itrane.healthcare.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;

public class ExcelBuilder extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		String fileName = (String) model.get("fileName");
		res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		exportVod(model, workbook);

	}

	private void exportVod(Map<String, Object> model, HSSFWorkbook workbook)
			throws Exception {
		@SuppressWarnings("unchecked")
		List<Vod> vods = (List<Vod>) model.get("vods");
		@SuppressWarnings("unchecked")
		List<VitalMst> vms = (List<VitalMst>) model.get("vms");

		HSSFSheet sheet = workbook.createSheet("バイタル");
		sheet.setDefaultColumnWidth(12);

		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

		HSSFRow header = sheet.createRow(0);
		HSSFCell cell = header.createCell(0);

		// ヘッダ
		cell.setCellValue("日付");
		cell.setCellStyle(cellStyle);
		int col = 1;
		for (VitalMst vm : vms) {
			cell = header.createCell(col++);
			cell.setCellValue(vm.getName());
		}

		int row = 1;
		for (Vod vod : vods) {
			HSSFRow arow = sheet.createRow(row++);

			cell = arow.createCell(0);
			cell.setCellValue(vod.getSokuteiBi());
			cell.setCellStyle(cellStyle);
			int c = 1;
			for (Vital vt : vod.getVitals()) {
				arow.createCell(c++).setCellValue(vt.getSokuteiTi());
			}
		}
	}

}
