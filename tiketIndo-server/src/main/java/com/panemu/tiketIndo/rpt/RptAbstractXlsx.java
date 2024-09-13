/*
 *  Intelectual Property of CV. Panemu Indonesia
 *  http://panemu.com
 */
package com.panemu.tiketIndo.rpt;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
public abstract class RptAbstractXlsx {

	private Logger log = LoggerFactory.getLogger(RptAbstractXlsx.class);

	protected abstract List<ExcelHeaderColumn> getColumns();

	protected abstract List<List<Object>> getData();

	protected String getTitle() {
		return "";
	}

	protected String getFilename() {
		return "report";
	}

	public void export(OutputStream os) {
		this.export(getTitle(), getColumns(), getData(), os);
	}

	protected void afterBody(XSSFSheet sheet, int rowIdx) {
	}

	protected XSSFWorkbook wb;
	protected XSSFCellStyle csText;
	protected XSSFCellStyle csDate;
	protected XSSFCellStyle csHeader;
	protected XSSFCellStyle csTitle;
	protected XSSFCellStyle csFooter;
	protected XSSFCellStyle csIntNum;
	protected XSSFCellStyle csDoubleNum;
	public static String NULL_LABEL = "";
	protected int totalRowHeader;

	private void export(String title, List<ExcelHeaderColumn> lstColumn, List<List<Object>> lstData, OutputStream outputStream) {
		totalRowHeader = totalRow(lstColumn, 0);

		List<ExcelHeaderColumn> lstLeafColumn = new ArrayList<>();
		lstLeafColumn = getLeafColumns(lstLeafColumn, lstColumn);
		try {
			int headerDepth = 1;
			for (ExcelHeaderColumn column : lstColumn) {
				int depth = getHeaderDepth(column, 1);
				headerDepth = Math.max(depth, headerDepth);
			}
			wb = new XSSFWorkbook();
			prepareStyle();
			// create a new sheet
			XSSFSheet sheet = wb.createSheet();
			int rowIdx = 0;
			XSSFRow row = sheet.createRow(rowIdx);

			XSSFCell cell = row.createCell(0);
			cell.setCellValue(title);
			CellRangeAddress cra = new CellRangeAddress(rowIdx, rowIdx, 0, lstLeafColumn.size() - 1);
			mergeCellAndSetStyle(sheet, cra, csTitle);

			row.setHeight((short) (row.getHeight() * 4));
			rowIdx++;
			rowIdx++;
			totalRowHeader = totalRowHeader + rowIdx;
			Map<Integer, XSSFRow> mapRow = new HashMap<>();
			createHeader(sheet, mapRow, lstColumn, 0, rowIdx);
			row = sheet.getRow(rowIdx + headerDepth - 1);
			int i = 0;
			for (ExcelHeaderColumn column : lstLeafColumn) {
				sheet.setColumnWidth(i, (int) (258 / 8 * column.getWidth()));
				cell = row.getCell(i);
				if (cell == null) {
					cell = row.createCell(i);
				}
				cell.setCellStyle(csHeader);
				i++;
			}

			rowIdx = rowIdx + headerDepth;

			for (List<Object> data : lstData) {
				row = sheet.createRow(rowIdx);
				int j = 0;
				for (Object value : data) {
					if (value == null) {
						value = NULL_LABEL;
					}
					cell = row.createCell(j);
					if (value instanceof Long || value instanceof Integer) {
						cell.setCellStyle(csIntNum);
						cell.setCellValue(Double.parseDouble(value.toString()));
					} else if (value instanceof Double || value instanceof Float || value instanceof BigDecimal) {
						double d = Double.parseDouble(value.toString());
						if (d % 1 == 0) {
							cell.setCellStyle(csIntNum);
						} else {
							cell.setCellStyle(csDoubleNum);
						}
						cell.setCellValue(d);
					} else if (value instanceof Date) {
						cell.setCellStyle(csDate);
						cell.setCellValue((Date) value);
					} else {
						cell.setCellStyle(csText);
						cell.setCellValue(value + "");
					}
					j++;
				}

				rowIdx++;
			}

			afterBody(sheet, rowIdx);

			wb.write(outputStream);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			try {
				wb.close();
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}

	}

	protected int totalRow(List<ExcelHeaderColumn> lstColumn, int total) {
		total++;
		int temp = 0;
		int temp2 = 0;
		for (ExcelHeaderColumn column : lstColumn) {
			if (!column.getNestedColumns().isEmpty()) {
				if (temp2 != 0) {
					temp = totalRow(column.getNestedColumns(), temp2);
				} else {
					temp = totalRow(column.getNestedColumns(), total);
				}

				if (temp > total) {
					temp2 = total;
					total = temp;
				}
			}
		}

		return total;
	}

	protected List<ExcelHeaderColumn> getLeafColumns(List<ExcelHeaderColumn> pool, List<ExcelHeaderColumn> columns) {
		for (ExcelHeaderColumn column : columns) {
			if (column.getNestedColumns() == null || column.getNestedColumns().isEmpty()) {
				pool.add(column);
			} else {
				getLeafColumns(pool, column.getNestedColumns());
			}
		}

		return pool;
	}

	protected int getHeaderDepth(ExcelHeaderColumn column, int depth) {
		if (column.getNestedColumns().isEmpty()) {
			return depth;
		}
		int result = depth;
		for (ExcelHeaderColumn clm : column.getNestedColumns()) {
			int newDepth = getHeaderDepth(clm, depth + 1);
			result = Math.max(result, newDepth);
		}

		return result;
	}

	protected void mergeCellAndSetStyle(XSSFSheet sheet, CellRangeAddress range, XSSFCellStyle style) {
		sheet.addMergedRegion(range);
		if (style != null) {
			setMergedCellStyle(style, sheet, range);
		}
	}

	protected void setMergedCellStyle(XSSFCellStyle style, XSSFSheet sheet, CellRangeAddress range) {

		int rowStart = range.getFirstRow();
		int rowEnd = range.getLastRow();
		int colStart = range.getFirstColumn();
		int colEnd = range.getLastColumn();
		for (int i = rowStart; i <= rowEnd; i++) {
			XSSFRow row = (XSSFRow) CellUtil.getRow(i, sheet);
			for (int col = colStart; col <= colEnd; col++) {
				XSSFCell cell = (XSSFCell) CellUtil.getCell(row, col);
				cell.setCellStyle(style);
			}
		}
	}

	protected int createHeader(XSSFSheet sheet, Map<Integer, XSSFRow> mapRow, List<ExcelHeaderColumn> header, int x, int y) {
		XSSFRow row = mapRow.get(y);
		if (row == null) {
			row = sheet.createRow(y);
			mapRow.put(y, row);
		}
		int lastX = 0;
		int spanX = 0;
		int spanY = 0;
		CellRangeAddress cellRangeAddress;

		for (int i = 0; i < header.size(); i++) {
			ExcelHeaderColumn column = header.get(i);
			if (!column.getNestedColumns().isEmpty()) {
				lastX = createHeader(sheet, mapRow, column.getNestedColumns(), x, y + 1);
				spanX = lastX - x;
				spanY = 1;
				XSSFCell cell = row.createCell(x);
				cell.setCellValue(column.getLabel());
				cellRangeAddress = new CellRangeAddress(y, y + spanY - 1, x, x + spanX - 1);
				x = lastX - 1;
				mergeCellAndSetStyle(sheet, cellRangeAddress, csHeader);
			} else {
				spanX = 1;
				spanY = totalRowHeader - y;
				XSSFCell cell = row.createCell(x);
				cell.setCellValue(column.getLabel());
				if (spanY > 1) {
					cellRangeAddress = new CellRangeAddress(y, y + spanY - 1, x, x + spanX - 1);
					mergeCellAndSetStyle(sheet, cellRangeAddress, csHeader);
				} else {
					cell.setCellStyle(csHeader);
				}
			}
			x++;
		}
		return x;
	}

	protected void setTotaRowHeader(int totalRowHeader) {
		this.totalRowHeader = totalRowHeader;
	}

	protected void prepareStyle() {
		csText = wb.createCellStyle();
//		csText.setDataFormat(XSSFDataFormat.getBuiltinFormat("text"));
		csText.setBorderBottom(BorderStyle.THIN);
		csText.setBorderLeft(BorderStyle.THIN);
		csText.setBorderRight(BorderStyle.THIN);
		csText.setBorderTop(BorderStyle.THIN);
		csText.setWrapText(true);
		csText.setVerticalAlignment(VerticalAlignment.CENTER);

		csDate = wb.createCellStyle();
		csDate.setBorderBottom(BorderStyle.THIN);
		csDate.setBorderLeft(BorderStyle.THIN);
		csDate.setBorderRight(BorderStyle.THIN);
		csDate.setBorderTop(BorderStyle.THIN);
		csDate.setWrapText(true);
		csDate.setVerticalAlignment(VerticalAlignment.CENTER);
		CreationHelper createHelper = wb.getCreationHelper();
		csDate.setDataFormat(
				  createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm"));

		csHeader = wb.createCellStyle();
//		csHeader.setDataFormat(XSSFDataFormat.getBuiltinFormat("text"));
		csHeader.setBorderBottom(BorderStyle.MEDIUM);
		csHeader.setBorderLeft(BorderStyle.MEDIUM);
		csHeader.setBorderRight(BorderStyle.MEDIUM);
		csHeader.setBorderTop(BorderStyle.MEDIUM);
		csHeader.setAlignment(HorizontalAlignment.CENTER);
		csHeader.setVerticalAlignment(VerticalAlignment.CENTER);
		XSSFFont f = wb.createFont();
		f.setBold(true);
		csHeader.setFont(f);

		csTitle = wb.createCellStyle();
//		csTitle.setDataFormat(XSSFDataFormat.getBuiltinFormat("text"));
		f = wb.createFont();
		f.setBold(true);
		csTitle.setFont(f);
		csTitle.setAlignment(HorizontalAlignment.CENTER);
		csTitle.setVerticalAlignment(VerticalAlignment.CENTER);

		csFooter = wb.createCellStyle();
		f = wb.createFont();
		f.setBold(true);
		csFooter.setFont(f);
		csFooter.setAlignment(HorizontalAlignment.RIGHT);
		csFooter.setVerticalAlignment(VerticalAlignment.CENTER);
		csFooter.setBorderBottom(BorderStyle.MEDIUM);
		csFooter.setBorderLeft(BorderStyle.MEDIUM);
		csFooter.setBorderRight(BorderStyle.MEDIUM);
		csFooter.setBorderTop(BorderStyle.MEDIUM);

		// -------INTEGER
		csIntNum = wb.createCellStyle();
		csIntNum.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		csIntNum.setBorderBottom(BorderStyle.THIN);
		csIntNum.setBorderLeft(BorderStyle.THIN);
		csIntNum.setBorderRight(BorderStyle.THIN);
		csIntNum.setBorderTop(BorderStyle.THIN);
		csIntNum.setVerticalAlignment(VerticalAlignment.CENTER);

		csDoubleNum = wb.createCellStyle();
		csDoubleNum.setDataFormat(wb.createDataFormat().getFormat("#,##0.##"));
		csDoubleNum.setBorderBottom(BorderStyle.THIN);
		csDoubleNum.setBorderLeft(BorderStyle.THIN);
		csDoubleNum.setBorderRight(BorderStyle.THIN);
		csDoubleNum.setBorderTop(BorderStyle.THIN);
		csDoubleNum.setVerticalAlignment(VerticalAlignment.CENTER);

	}

	protected void indentCell(XSSFCell cell, int indentValue) {
		CellUtil.setAlignment(cell, HorizontalAlignment.LEFT);
		CellUtil.setCellStyleProperty(cell, CellUtil.INDENTION, (short) (indentValue));
	}
}
