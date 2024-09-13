package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.Member;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
public class DtoUserList {

	private static Logger log = LoggerFactory.getLogger(DtoUserList.class);
	public int id;
	public String username;
	public String email;
	public String password;
	public String role;
	public String koordinator;
	public int version;

	public static DtoUserList create(Member rcd) {
		DtoUserList dto = new DtoUserList();
		dto.id = rcd.getId();
		dto.username = rcd.getUsername();
		dto.email = rcd.getEmail();
		dto.password = rcd.getPassword();
		dto.role = rcd.getRole();
		dto.koordinator = rcd.getKoordinator();
		if (dto.version != 0) {
			try {
				dto.version = rcd.getVersion();
			} catch (EntityNotFoundException ex) {
				log.error(ex.getMessage());
			}
		}
		return dto;
	}
}
