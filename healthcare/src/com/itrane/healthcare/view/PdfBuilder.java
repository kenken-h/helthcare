package com.itrane.healthcare.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.itrane.healthcare.model.Vital;
import com.itrane.healthcare.model.VitalMst;
import com.itrane.healthcare.model.Vod;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class PdfBuilder extends AbstractPdfView{

	private Rectangle size;
	private PdfContentByte cb;
	private BaseFont bf;

	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		
		//初期処理
		document.setPageSize(PageSize.A4.rotate());
		document.open();
		
        PdfTableLayout event = new PdfTableLayout();

		bf = BaseFont.createFont("/Library/Fonts/Hiragino Sans GB W3.otf", 
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		cb = writer.getDirectContent();
        float fontSize = 7;
		cb.setFontAndSize(bf, fontSize);		
		size = document.getPageSize();

		//モデルから出力データを取得
		@SuppressWarnings("unchecked")
		List<Vod> vods = (List<Vod>) model.get("vods");
		List<VitalMst> vms = (List<VitalMst>)model.get("vms");

		// ==== テーブル全体の設定 ====
        PdfPTable table = new PdfPTable(1 + vms.size());
        float widths[] = new float[1 + vms.size()];
        widths[0] = 1.0f;
        for (int i=1; i< vms.size()+1; i++) {
        	widths[i] = 0.8f;
        }
        table.setWidths(widths);
        //　テーブル全体の幅：
        table.setWidthPercentage(100);
        //　パディング設定
        table.getDefaultCell().setPaddingTop(bf.getFontDescriptor(BaseFont.ASCENT, fontSize) - fontSize + 2);
        table.getDefaultCell().setPaddingLeft(5);
        table.getDefaultCell().setPaddingRight(5);
        table.getDefaultCell().setPaddingBottom(3);
        //　ボーダー設定
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        //　---- テーブルヘッダ (種別) ----
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(new Phrase("バイタル", new Font(bf, fontSize)));
		for (VitalMst vm: vms) {
	        table.addCell(new Phrase(vm.getName(), new Font(bf, fontSize * 0.9f)));
		}
        //　---- テーブルヘッダ (予定時間) ----
        table.addCell(new Phrase("測定時間", new Font(bf, fontSize * 0.9f)));
		for (VitalMst vm: vms) {
	        table.addCell(new Phrase(vm.getJikan(), new Font(bf, fontSize * 0.9f)));
		}
				//データの行
		for (Vod vod: vods) {
			String sokuteiBi = vod.getSokuteiBi();
	        table.addCell(new Phrase(sokuteiBi, new Font(bf, fontSize * 0.9f)));
			for(Vital vt: vod.getVitals()) {
		        table.addCell(new Phrase(vt.getSokuteiTi(), new Font(bf, fontSize * 0.9f)));
			}
		}
        
        table.setTableEvent(event);
        table.setHeaderRows(1);
        document.add(table);

	}
}


