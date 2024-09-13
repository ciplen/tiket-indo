package com.panemu.tiketIndo.common;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author amrullah
 */
public class SaltedJdbcRealm extends JdbcRealm {

	private static SaltedJdbcRealm instance;
	public SaltedJdbcRealm() {
		instance = this;
		setSaltStyle(SaltStyle.COLUMN);
	}

	public AuthorizationInfo getSubjectInfo(Subject subject) {
		return this.doGetAuthorizationInfo(subject.getPrincipals());
	}

	public static SaltedJdbcRealm getInstance() {
		return instance;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		if (auth instanceof JwtToken) {
			String token = (String) auth.getCredentials();
			String username = JwtUtil.getUsername(token);
			if (username == null) {
				throw new AuthenticationException("token invalid");
			}

			if (!JwtUtil.verify(token, username)) {
				throw new AuthenticationException("Username or password error");
			}

			return new SimpleAuthenticationInfo(username, token, "jwt_realm");
		}
		return super.doGetAuthenticationInfo(auth);
	}

	public boolean supports(AuthenticationToken token) {
		return token != null && (getAuthenticationTokenClass().isAssignableFrom(token.getClass()) || JwtToken.class.isAssignableFrom(token.getClass()));
	}

	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
		if (getAuthenticationTokenClass().isAssignableFrom(token.getClass())) {
			super.assertCredentialsMatch(token, info);
		}
	}	
}
