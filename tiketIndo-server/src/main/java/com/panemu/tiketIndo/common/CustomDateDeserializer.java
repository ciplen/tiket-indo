package com.panemu.tiketIndo.common;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author amrullah
 */
public class CustomDateDeserializer implements JsonbDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext dc, Type type) {
		Date result = null;
		String stringDate = jp.getString();
		if (StringUtils.isNumeric(stringDate)) {
			result = new Date(Long.parseLong(stringDate));
		} else if (stringDate != null && !stringDate.isEmpty()) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				result = df.parse(stringDate);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}
		}
		return result;
	}

}
