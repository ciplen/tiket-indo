package com.panemu.tiketIndo.rpt;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amrullah
 */
public class ExcelHeaderColumn {
	private String label;
	private int width;
	private List<ExcelHeaderColumn> nestedColumns = new ArrayList<ExcelHeaderColumn>();

	public ExcelHeaderColumn(String label, int width) {
		this.label = label;
		this.width = width;
	}

	public ExcelHeaderColumn() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public List<ExcelHeaderColumn> getNestedColumns() {
		return nestedColumns;
	}

	public void setNestedColumns(List<ExcelHeaderColumn> nestedColumns) {
		this.nestedColumns = nestedColumns;
	}
	
}
