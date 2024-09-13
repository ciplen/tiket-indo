package com.panemu.tiketIndo.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author amrullah
 */
public class CustomDateSerializer implements JsonbSerializer<Date>{

	@Override
	public void serialize(Date t, JsonGenerator jg, SerializationContext sc) {
		if (t != null) {
			if (t instanceof java.sql.Date) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				sc.serialize(df.format(t), jg);
			} else {
				sc.serialize(t.getTime(), jg);
			}
		} else {
			sc.serialize(null, jg);
		}
	}
	
}
