package com.panemu.tiketIndo.rpt;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.rcd.CountryData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author mubin
 */
public class RptCountryList extends RptAbstractXlsx {

	TableData<CountryData> data;
	private int startIndex;
	private int maxRecord;

	public RptCountryList(TableData<CountryData> data, int startIndex, int maxRecord) {
		this.data = data;
		this.startIndex = startIndex;
		this.maxRecord = maxRecord;
	}

	@Override
	protected List<ExcelHeaderColumn> getColumns() {
		List<ExcelHeaderColumn> lst = new ArrayList<>();
		lst.add(new ExcelHeaderColumn("Nmr.", 60));
		lst.add(new ExcelHeaderColumn("Country Name", 200));
		lst.add(new ExcelHeaderColumn("Capital City", 200));
		lst.add(new ExcelHeaderColumn("Continent", 200));
		lst.add(new ExcelHeaderColumn("Independence Day", 200));
		lst.add(new ExcelHeaderColumn("Population", 200));
		return lst;
	}

	@Override
	protected List<List<Object>> getData() {
		List<List<Object>> rows = new ArrayList<>();

		for (int i = 0; i < data.getRows().size(); i++) {
			CountryData rcd = data.getRows().get(i);
			List<Object> row = new ArrayList<>();
			row.add(startIndex + i + 1);
			row.add(rcd.getName());
			row.add(rcd.getCapital());
			row.add(rcd.getContinent());
			row.add(rcd.getIndependence());
			row.add(rcd.getPopulation());
			rows.add(row);
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
		cell.setCellValue("Data " + (startIndex + 1) + " - " + endIndex + " dari total " + data.getTotalRows());
	}

	@Override
	protected String getTitle() {
		DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm");
		Date date = new Date();
		String tglStatus = df.format(date);

		return "Country \n Status: " + tglStatus;
	}
}
