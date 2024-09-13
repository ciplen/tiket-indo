package com.panemu.tiketIndo.common;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Mubin .N <nur.mubin@panemu.com>
 * @param <T>
 */
public class TableData<T> implements Serializable {

	private final List<T> rows;
	private final long totalRows;

	public TableData(List<T> rows, long totalRows) {
		this.rows = rows;
		this.totalRows = totalRows;
	}

	public List<T> getRows() {
		return rows;
	}

	public long getTotalRows() {
		return totalRows;
	}

}
