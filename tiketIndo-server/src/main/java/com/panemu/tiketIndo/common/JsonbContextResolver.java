package com.panemu.tiketIndo.common;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author amrullah
 */
@Provider
public class JsonbContextResolver implements ContextResolver<Jsonb> {

	private final Jsonb jsonB;

	public JsonbContextResolver() {
		JsonbConfig config = new JsonbConfig().withSerializers( new CustomDateSerializer()).withDeserializers(new CustomDateDeserializer());
		jsonB = JsonbBuilder.create(config);
	}

	@Override
	public Jsonb getContext(Class<?> type) {
		return jsonB;
	}

}
