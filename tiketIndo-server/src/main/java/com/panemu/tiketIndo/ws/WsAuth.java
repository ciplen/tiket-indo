package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.common.JwtUtil;
import com.panemu.tiketIndo.common.SaltedJdbcRealm;
import com.panemu.tiketIndo.dto.DtoAuth;
import com.panemu.tiketIndo.dto.DtoLogin;
import com.panemu.tiketIndo.rcd.LogSystem;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.Member;
import com.panemu.tiketIndo.srv.SrvLogSystem;
import com.panemu.tiketIndo.srv.SrvMember;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsAuth {

	private Logger log = LoggerFactory.getLogger(WsAuth.class);
	@Inject
	private SrvMember srvMember;
	@Inject
	private SrvLogSystem srvLog;

	private static String getClientIp(HttpServletRequest request) {

		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}

		return remoteAddr;
	}

	@POST
	@Path("login")
	@Produces({"application/json"})
	@Consumes({"application/json"})
	public DtoAuth login(DtoLogin loginDto, @Context HttpServletRequest request) {
		log.info("logging in: " + loginDto.toString());
		boolean loggedin = userLoggin(loginDto.getUsername(), loginDto.getPassword());
		if (!loggedin) {
			throw GeneralException.create(Response.Status.UNAUTHORIZED);
		} else {
			Subject currentUser = SecurityUtils.getSubject();
			AuthorizationInfo subjectInfo = SaltedJdbcRealm.getInstance().getSubjectInfo(currentUser);
			DtoAuth auth = new DtoAuth();
			Member user = srvMember.getMemberByMemberName(loginDto.getUsername());
			auth.setId(user.getId());
			auth.setUsername(loginDto.getUsername());
			String jwt = JwtUtil.sign(loginDto.getUsername());
			auth.setJwt(jwt);
			if (!CollectionUtils.isEmpty(subjectInfo.getRoles())) {
				auth.setRole(subjectInfo.getRoles().iterator().next());
			}
			auth.setPermissions(subjectInfo.getStringPermissions());
			String remoteIp = getClientIp(request);
			LogSystem logSystem = new LogSystem("SUKSES", loginDto.getUsername(), "Login", "LOGIN Country App Seed ip-address: " + remoteIp, new Date());
			srvLog.insertLogSystem(logSystem);
			return auth;
		}
	}

	private boolean userLoggin(String username, String password) {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			currentUser.logout();
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		token.setRememberMe(false);
		try {
			currentUser.login(token);
			return true;
		} catch (UnknownAccountException uae) {
			log.error(uae.getMessage());
		} catch (AuthenticationException ae) {
			log.error(ae.getMessage());
		}
		return false;
	}

	@POST
	@Path("logout")
	public void logout(@Context HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		Object obj = currentUser.getPrincipal();
		try {
			if (obj != null && currentUser.isAuthenticated()) {
				currentUser.logout();
				String remoteIp = getClientIp(request);
				LogSystem logSystem = new LogSystem("SUKSES", currentUser.getPrincipals().toString(), "Logout", "LOGOUT Country App Seed ip-address: " + remoteIp, new Date());
				srvLog.insertLogSystem(logSystem);
			} else {
				log.info("User is already logged out: " + obj);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

	}

	/**
	 * Used to check if a request is authenticated. Otherwise it will throw 401
	 * error
	 */
	@GET
	@Path("check")
	@RequiresAuthentication
	public void check() {
		Subject currentUser = SecurityUtils.getSubject();
		log.debug("auth check: " + currentUser.getPrincipal() + "");
	}
}
