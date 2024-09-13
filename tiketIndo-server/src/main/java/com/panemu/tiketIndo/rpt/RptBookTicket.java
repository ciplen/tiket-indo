package com.panemu.tiketIndo.rpt;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.rcd.BookTicket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author rofiq
 */
public class RptBookTicket extends RptAbstractXlsx {

	TableData<BookTicket> data;
	private int startIndex;
	private int maxRecord;
	private Date startDate;
	private Date endDate;

	public RptBookTicket(TableData<BookTicket> data, int startIndex, int maxRecord, Date startDate, Date endDate) {
		this.data = data;
		this.startIndex = startIndex;
		this.maxRecord = maxRecord;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	protected List<ExcelHeaderColumn> getColumns() {
		List<ExcelHeaderColumn> lst = new ArrayList<>();
		lst.add(new ExcelHeaderColumn("Nmr.", 60));
		lst.add(new ExcelHeaderColumn("Nama", 200));
		lst.add(new ExcelHeaderColumn("No Identitas", 200));
		lst.add(new ExcelHeaderColumn("No Telepon", 200));
		lst.add(new ExcelHeaderColumn("Email", 200));
		lst.add(new ExcelHeaderColumn("Event", 200));
		lst.add(new ExcelHeaderColumn("Kode Pesanan", 200));
		lst.add(new ExcelHeaderColumn("Tipe Ticket", 200));
		lst.add(new ExcelHeaderColumn("Rek Transfer", 200));
		lst.add(new ExcelHeaderColumn("No Rek", 200));
		lst.add(new ExcelHeaderColumn("Harga", 200));
		lst.add(new ExcelHeaderColumn("Kode Unik", 200));
		lst.add(new ExcelHeaderColumn("Status", 200));
		lst.add(new ExcelHeaderColumn("Modified By", 200));
		lst.add(new ExcelHeaderColumn("Creared Date", 200));
		lst.add(new ExcelHeaderColumn("Modified Date", 200));
		return lst;
	}

	@Override
	protected List<List<Object>> getData() {
		List<List<Object>> rows = new ArrayList<>();

		for (int i = 0; i < data.getRows().size(); i++) {
			BookTicket rcd = data.getRows().get(i);
//			this.startDate = rcd.getModifiedDate();
			if (rcd.getStatus().equals("APPROVED")) {
				List<Object> row = new ArrayList<>();

				row.add(startIndex + i + 1);
				row.add(rcd.getNama());
				row.add(rcd.getNoKtp());
				row.add(rcd.getNoTelepon());
				row.add(rcd.getEmail());
				row.add(rcd.getEvent());
				row.add(rcd.getId().toString());
				row.add(rcd.getTypeTicket());
				row.add(rcd.getRekTujuan());
				row.add(rcd.getNoRek());
				row.add(rcd.getHarga());
				row.add(rcd.getKodeUnik());
				row.add(rcd.getStatus());
				row.add(rcd.getModifiedBy());
				row.add(rcd.getCreatedDate());
				row.add(rcd.getModifiedDate());
				rows.add(row);
			}
		}
		return rows;
	}

	protected void afterBody(XSSFSheet sheet, int rowIdx) {

		rowIdx++;
		XSSFCellStyle csFooter = wb.createCellStyle();
		csFooter.setWrapText(true);
		csFooter.setVerticalAlignment(VerticalAlignment.CENTER);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowIdx, rowIdx, 0, 2);
		mergeCellAndSetStyle(sheet, cellRangeAddress, csFooter);
		long endIndex = Math.min(startIndex + maxRecord, data.getTotalRows());
		XSSFRow row = sheet.createRow(rowIdx);
		XSSFCell cell = row.createCell(0);
		cell.setCellStyle(csFooter);
//		cell.setCellValue("Data " + (startIndex + 1) + " - " + endIndex + " dari total " + data.getTotalRows());
		DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm");
		Date date = new Date();
		String tglStatus = df.format(date);
		cell.setCellValue("Tanggal export : " + tglStatus);
	}

	@Override
	protected String getFilename() {
		return "Laporan Keuangan";
	}

	@Override
	protected String getTitle() {
		Locale id = new Locale("in", "ID");
		String pattern = "dd MMMM yyyy";
		DateFormat df = new SimpleDateFormat(pattern, id);
		Date strDate = this.startDate;
		Date expDate = this.endDate;
		if (strDate == null) {
			strDate = new Date();
			String now = df.format(strDate);
			return "Laporan Keuangan \n tanggal: " + now;
		} else if (expDate == null) {
			String startDate = df.format(strDate);
			return "Laporan Keuangan \n tanggal: " + startDate;
		} 
		String startDate = df.format(strDate);
		String endDate = df.format(expDate);
		return "Laporan Keuangan \n tanggal: " + startDate + " - " + endDate;

	}

}
