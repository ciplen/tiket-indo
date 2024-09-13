package com.panemu.tiketIndo.rpt;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.rcd.Role;
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
 * @author amrullah
 */
public class RptRoleList extends RptAbstractXlsx {

	private TableData<Role> data;
	private int startIndex;
	private int maxRecord;

	public RptRoleList(TableData<Role> data, int startIndex, int maxRecord) {
		this.data = data;
		this.startIndex = startIndex;
		this.maxRecord = maxRecord;
	}

	@Override
	protected List<ExcelHeaderColumn> getColumns() {
		List<ExcelHeaderColumn> lst = new ArrayList<>();
		lst.add(new ExcelHeaderColumn("Nmr.", 60));
		lst.add(new ExcelHeaderColumn("Name", 200));
		lst.add(new ExcelHeaderColumn("Description", 200));
		return lst;
	}

	@Override
	protected List<List<Object>> getData() {
		List<List<Object>> rows = new ArrayList<>();

		for (int i = 0; i < data.getRows().size(); i++) {
			Role dto = data.getRows().get(i);
			List<Object> row = new ArrayList<>();
			row.add(startIndex + i + 1);
			row.add(dto.getName());
			row.add(dto.getDescription());
			rows.add(row);
		}

		return rows;
	}

	@Override
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

		return "Role \n Status: " + tglStatus;
	}
}
