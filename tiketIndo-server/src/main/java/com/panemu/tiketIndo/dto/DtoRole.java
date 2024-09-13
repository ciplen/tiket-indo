package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.Permission;
import com.panemu.tiketIndo.rcd.Role;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amrullah
 */
public class DtoRole {

	public int id;
	public int version;
	public String name;
	public String description;
	public List<PermissionInfo> permissionList = new ArrayList<>();

	public static DtoRole create(Role rcd, List<Permission> lstPermission) {
		DtoRole dto = new DtoRole();
		if (rcd != null) {
			dto.id = rcd.getId();
			dto.version = rcd.getVersion();
			dto.name = rcd.getName();
			dto.description = rcd.getDescription();
		}

		for (Permission availablePerm : lstPermission) {
			PermissionInfo pi = new PermissionInfo();
			pi.id = availablePerm.getId();
			pi.permission = availablePerm.getCode();
			pi.description = availablePerm.getDescription();
			pi.selected = false;
			if (rcd != null) {
				for (Permission assignedPerm : rcd.getPermissionList()) {
					if (assignedPerm.getCode().equals(availablePerm.getCode())) {
						pi.selected = true;
						break;
					}
				}
			}
			dto.permissionList.add(pi);
		}

		return dto;
	}

	public static class PermissionInfo {

		private int id;
		private String permission;
		private String description;
		private boolean selected;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getPermission() {
			return permission;
		}

		public void setPermission(String permission) {
			this.permission = permission;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}
}
