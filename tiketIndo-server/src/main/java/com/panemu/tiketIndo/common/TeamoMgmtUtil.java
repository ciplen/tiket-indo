package com.panemu.tiketIndo.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author amrullah
 */
public class TeamoMgmtUtil {

	public static int toIntegerPrimitive(String stringNumber, int defaultValue) {
		try {
			int value = Integer.parseInt(stringNumber);
			return value;
		} catch (NumberFormatException numberFormatException) {
			return defaultValue;
		}
	}

	public static int toIntegerPrimitive(Integer number) {
		if (number == null) {
			return 0;
		}
		return number;
	}

	public static double toDoublePrimitive(Double number) {
		return toDoublePrimitive(number, 0d);
	}

	public static double toDoublePrimitive(Double number, double defaultValue) {
		if (number == null || number.isNaN()) {
			return defaultValue;
		}
		return number;
	}

	public static long toLongPrimitive(Long number) {
		if (number == null) {
			return 0;
		}
		return number;
	}

	public static boolean toBooleanPrimitive(Boolean bool) {
		if (bool == null) {
			return false;
		}
		return bool;
	}

	public static void configureExcelResponse(HttpServletResponse response, String fileName) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmm");
		String date = df.format(new Date());
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + "_" + date + ".xlsx");
	}

	public static void configureCsvResponse(HttpServletResponse response, String fileName) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmm");
		String date = df.format(new Date());
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + "_" + date + ".csv");
	}

}
