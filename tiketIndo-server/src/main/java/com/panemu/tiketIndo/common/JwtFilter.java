package com.panemu.tiketIndo.common;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.apache.shiro.subject.support.DefaultSubjectContext;

public class JwtFilter extends BasicHttpAuthenticationFilter {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Judge if the user wanna login or not
	 */
	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
//		log.info("isLoginAttempt");
		HttpServletRequest req = (HttpServletRequest) request;
		String authorization = req.getHeader("Authorization");
		return authorization != null;
	}

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.FALSE);
		return super.onPreHandle(request, response, mappedValue);
	}
	
	

	/**
	 *
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//		log.info("executeLogin true");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String authorization = httpServletRequest.getHeader("Authorization");

		JwtToken token = new JwtToken(authorization);
		getSubject(request, response).login(token);
		return true;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isLoginAttempt(request, response)) {
			try {
				executeLogin(request, response);
				return true;
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return true;
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		if (httpServletRequest.getMethod().equals("OPTIONS")) {
			httpServletResponse.setStatus(Response.Status.OK.getStatusCode());
			return true;
		}
		return super.preHandle(request, response);
	}

}
