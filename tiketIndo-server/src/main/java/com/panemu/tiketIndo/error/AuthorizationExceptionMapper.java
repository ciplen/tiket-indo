package com.panemu.tiketIndo.error;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Provider
public class AuthorizationExceptionMapper implements ExceptionMapper<AuthorizationException> {

	private Logger log = LoggerFactory.getLogger(AuthorizationExceptionMapper.class);

	@Override
	public Response toResponse(AuthorizationException exception) {
		log.error("authentication exception: " + exception.getMessage());
		ErrorEntity error = new ErrorEntity();
		Subject currentUser = SecurityUtils.getSubject();
		if (StringUtils.contains(exception.getMessage(), "not authenticated")
				  || StringUtils.contains(exception.getMessage(), "subject is anonymous")) {
			error.setCode(ErrorCode.ER0401);
			return Response.status(Response.Status.UNAUTHORIZED).entity(error).type(MediaType.APPLICATION_JSON).build();
		} else {
			//Subject does not have permission [x:y]
			error.setCode(ErrorCode.ER0403);
			if (currentUser.hasRole("blocked")) {
				error.setParameters(new String[]{"Anda tidak diijinkan melakukan penjualan tiket, silahkan hubungi koordinator pusat!"});
			}
			return Response.status(Response.Status.FORBIDDEN).entity(error).type(MediaType.APPLICATION_JSON).build();
		}
	}

}
