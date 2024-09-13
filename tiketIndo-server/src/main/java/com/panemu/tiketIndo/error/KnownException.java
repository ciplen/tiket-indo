package com.panemu.tiketIndo.error;

import javax.ejb.ApplicationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 *
 * @author amrullah
 */
@ApplicationException(rollback = true)
public class KnownException extends ClientErrorException {

	public KnownException(Response response) {
		super(response);
	}
	
}
