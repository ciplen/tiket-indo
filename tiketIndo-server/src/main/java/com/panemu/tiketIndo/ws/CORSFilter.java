package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.common.CommonUtil;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

	private static Logger log = LoggerFactory.getLogger(CORSFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext cres) throws IOException {
//		cres.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");
//		cres.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:8100");
		cres.getHeaders().add("Access-Control-Allow-Origin", requestContext.getHeaderString("Origin"));
		cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, platform");
		cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
		cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		cres.getHeaders().add("Access-Control-Max-Age", "1209600");

		if (CommonUtil.isDevMode()) {
			log.info("DEVELOPMENT MODE, creating delay");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
		}
	}

}
