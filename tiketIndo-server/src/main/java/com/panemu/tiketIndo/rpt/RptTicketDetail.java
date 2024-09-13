package com.panemu.tiketIndo.rpt;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.rcd.TicketDtl;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author bastomi
 */
public class RptTicketDetail extends RptAbstractXlsx {

	TableData<TicketDtl> data;
	private int startIndex;
	private int maxRecord;

	public RptTicketDetail(TableData<TicketDtl> data, int startIndex, int maxRecord) {
		this.data = data;
		this.startIndex = startIndex;
		this.maxRecord = maxRecord;
	}

	@Override
	protected List<ExcelHeaderColumn> getColumns() {
		List<ExcelHeaderColumn> lst = new ArrayList<>();
		lst.add(new ExcelHeaderColumn("Nmr.", 60));
		lst.add(new ExcelHeaderColumn("Nama", 200));
		lst.add(new ExcelHeaderColumn("No Identitas", 200));
		lst.add(new ExcelHeaderColumn("Email", 200));
		lst.add(new ExcelHeaderColumn("Tipe", 200));
		lst.add(new ExcelHeaderColumn("Status", 200));
		lst.add(new ExcelHeaderColumn("Modified By", 200));
		lst.add(new ExcelHeaderColumn("Modified Date", 200));
		return lst;
	}

	@Override
	protected List<List<Object>> getData() {
		List<List<Object>> rows = new ArrayList<>();

		for (int i = 0; i < data.getRows().size(); i++) {
			TicketDtl rcd = data.getRows().get(i);
			List<Object> row = new ArrayList<>();
			row.add(startIndex + i + 1);
			row.add(rcd.getBuyerName());
			row.add(rcd.getNoKtp());
			row.add(rcd.getBuyerEmail());
			row.add(rcd.getType());
			row.add(rcd.getStatus());
			row.add(rcd.getModifiedBy());
			row.add(rcd.getModifiedDate());
			rows.add(row);
		}
		return rows;
	}

	@Override
	protected String getFilename() {
		return "Tiket Detail";
	}

	@Override
	protected String getTitle() {
		Locale id = new Locale("in", "ID");
		String pattern = "dd MMMM yyyy";
		DateFormat df = new SimpleDateFormat(pattern, id);
		Date strDate = new Date();
		String now = df.format(strDate);
		return "Tiket Detail \n Tanggal: " + now;
	}
}
