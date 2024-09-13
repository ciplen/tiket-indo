package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.common.CommonUtil;
import java.util.List;

/**
 *
 * @author Heru Tri Julianto<heru.trijulianto@panemu.com>
 */
public class DtoExcel {

	List<String> lstHeader;
	List<List<String>> lstRow;
	String[] lstColumn;

	public DtoExcel(List<String> lstHeader, List<List<String>> lstRow) {
		this.lstHeader = lstHeader;
		this.lstRow = lstRow;
	}

	public List<String> getLstHeader() {
		return lstHeader;
	}

	public void setLstHeader(List<String> lstHeader) {
		this.lstHeader = lstHeader;
	}

	public List<List<String>> getLstRow() {
		return lstRow;
	}

	public void setLstRow(List<List<String>> lstRow) {
		this.lstRow = lstRow;
	}

	public String[] getLstColumn() {
		return lstColumn;
	}

	public void setLstColumn(String[] lstColumn) {
		this.lstColumn = lstColumn;
	}

}
