package com.panemu.tiketIndo.error;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
public class GeneralException {
	private static Logger log = LoggerFactory.getLogger(GeneralException.class);
	private GeneralException(){}
	
	public static KnownException create(ErrorEntity errorEntity) {
		log.error(errorEntity.getMessage());
		final KnownException badRequestException = new KnownException(Response.status(Response.Status.BAD_REQUEST)
				  .entity(errorEntity).type(MediaType.APPLICATION_JSON).build());
		return badRequestException;
	}
	
	public static KnownException create(Response.Status status) {
		log.error("error status: " + status);
		final KnownException badRequestException = new KnownException(Response.status(status)
				  .entity("") // this entity("") is needed to avoid log in console on wildfly 16
				  .build());
		return badRequestException;
	}

}
