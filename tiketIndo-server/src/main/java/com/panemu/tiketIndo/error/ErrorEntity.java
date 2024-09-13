package com.panemu.tiketIndo.error;

import java.util.MissingFormatArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
public class ErrorEntity {
	
	private Logger log = LoggerFactory.getLogger(ErrorEntity.class);
	private ErrorCode code;
	private String[] parameters;
	
	public ErrorEntity() {
		
	}

	public ErrorEntity(ErrorCode code, String... parameters) {
		this.code = code;
		this.parameters = parameters;
	}
	
	public ErrorCode getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	
	public String getMessage() {
		try {
			return String.format(code.getMessage(), (Object[]) parameters);
		} catch (MissingFormatArgumentException ex) {
			log.warn(ex.getMessage() + ". " + code.getMessage());
			return code.getMessage();
		}
	}
	
}
