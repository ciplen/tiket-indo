package com.panemu.tiketIndo.error;

import javax.ejb.EJBTransactionRolledbackException;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
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
public class DatabaseExceptionMapper implements ExceptionMapper<EJBTransactionRolledbackException> {

	private Logger log = LoggerFactory.getLogger(DatabaseExceptionMapper.class);
	@Context
	private HttpServletRequest request;

	////EJBTransactionRolledbackException
	@Override
	public Response toResponse(EJBTransactionRolledbackException exception) {
		if (exception.getCause() instanceof OptimisticLockException
						|| (exception.getCause() != null && exception.getCause().getCause() instanceof OptimisticLockException)) {
			log.error("Optimistic locking : " + exception.getMessage());
			ErrorEntity ee = new ErrorEntity(ErrorCode.ER0100, (String[]) null);
			return Response.status(Response.Status.CONFLICT).entity(ee).type(MediaType.APPLICATION_JSON).build();
		} else {
			String requestInfo = request.getMethod() + " " + request.getPathInfo();
			if (request.getQueryString() != null) {
				requestInfo = requestInfo + "?" + request.getQueryString();
			}
			String message = getMessage(exception, 10);
			log.error(requestInfo, exception);
			return Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.APPLICATION_JSON).build();
		}
	}

	private String getMessage(Throwable t, int depth) {
		String m = t.getMessage();
		if (depth > 0 && t.getCause() != null) {
			m = this.getMessage(t.getCause(), depth - 1) + "; " + m;
		}
		return m;
	}

}
