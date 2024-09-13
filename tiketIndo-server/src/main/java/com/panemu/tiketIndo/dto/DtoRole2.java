package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.Role;
import java.util.List;

/**
 *
 * @author amrullah
 */
public class DtoRole2 {

	private int roleId;
	private String roleName;
	private String roleDesc;
	private List<String> lstPermission;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public List<String> getLstPermission() {
		return lstPermission;
	}

	public void setLstPermission(List<String> lstPermission) {
		this.lstPermission = lstPermission;
	}
}
