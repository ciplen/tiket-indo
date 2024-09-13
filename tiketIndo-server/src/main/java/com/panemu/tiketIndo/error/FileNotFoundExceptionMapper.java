package com.panemu.tiketIndo.error;

import java.io.FileNotFoundException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Provider
public class FileNotFoundExceptionMapper implements ExceptionMapper<FileNotFoundException> {

	private Logger log = LoggerFactory.getLogger(FileNotFoundExceptionMapper.class);

	@Override
	public Response toResponse(FileNotFoundException exception) {
		log.error(exception.getClass() + ": " + exception.getMessage());
		return Response.status(Response.Status.NOT_FOUND).build();
	}

}
