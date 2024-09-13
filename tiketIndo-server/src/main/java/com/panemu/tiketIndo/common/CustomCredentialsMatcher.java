package com.panemu.tiketIndo.common;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 *
 * @author amrullah
 */
public class CustomCredentialsMatcher implements CredentialsMatcher {

	
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		Object submittedPassword = getSubmittedPassword(token);
		Object storedCredentials = getStoredPassword(info);
		String pwd = CommonUtil.hashText(new String((char[]) submittedPassword));
		return pwd.equals(storedCredentials);
	}

	protected Object getSubmittedPassword(AuthenticationToken token) {
		return token != null ? token.getCredentials() : null;
	}

	protected Object getStoredPassword(AuthenticationInfo storedAccountInfo) {
		Object stored = storedAccountInfo != null ? storedAccountInfo.getCredentials() : null;
		if (stored instanceof char[]) {
			stored = new String((char[]) stored);
		}
		return stored;
	}
	
}
