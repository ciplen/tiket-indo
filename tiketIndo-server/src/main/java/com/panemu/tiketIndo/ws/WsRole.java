package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoRole;
import com.panemu.tiketIndo.rcd.Permission;
import com.panemu.tiketIndo.rcd.Role;
import com.panemu.tiketIndo.rpt.RptRoleList;
import com.panemu.tiketIndo.srv.SrvAuth;
import com.panemu.search.TableQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsRole {

	private Logger log = LoggerFactory.getLogger(WsRole.class);

	@Inject
	private SrvAuth srvAuth;

	@POST
	@RequiresAuthentication
	@RequiresPermissions(value = {"role:read", "role:write"}, logical = Logical.OR)
	public TableData<Role> findAll(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		TableData<Role> lstResult = srvAuth.findRole(tq, startIndex, maxRecord);

		lstResult.getRows().forEach(rcd -> {
			srvAuth.detach(rcd);
			rcd.setPermissionList(null);
		});

		return lstResult;
	}

	@GET
	@Path("{id}")
	@RequiresAuthentication
	@RequiresPermissions(value = {"role:read", "role:write"}, logical = Logical.OR)
	public DtoRole getRoles(@PathParam("id") int id) {
		DtoRole dto = null;
		Role role = null;
		if (id > 0) {
			role = srvAuth.findRole(id);
		}
		List<Permission> lstPermission = srvAuth.findPermissions();
		dto = DtoRole.create(role, lstPermission);

		return dto;
	}

	@PUT
	@Path("{id}")
	@RequiresPermissions(value = {"role:write"})
	public Role saveRole(@PathParam("id") Integer id, DtoRole dtoRole) {
		Subject currentUser = SecurityUtils.getSubject();
		Role role = null;
		if (id > 0) {
			role = srvAuth.findRole(dtoRole.id);
			role.setVersion(dtoRole.version);
		} else {
			role = new Role();
			role.setName(dtoRole.name);
		}
		role.setDescription(dtoRole.description);
		role.setModifiedBy(currentUser.getPrincipal() + "");
		role.setModifiedDate(new Date());
		role.setPermissionList(new ArrayList<>());
		srvAuth.detach(role);
		for (DtoRole.PermissionInfo pi : dtoRole.permissionList) {
			if (!pi.isSelected()) {
				continue;
			}
			Permission perm = new Permission();
			perm.setId(pi.getId());
			perm.setCode(pi.getPermission());
			role.getPermissionList().add(perm);
		}
		if (id > 0) {
			srvAuth.updateRole(role);
		} else {
			srvAuth.insertRole(role);
		}
		return role;
	}

	@DELETE
	@Path("{roleId}/{version}")
	@RequiresPermissions(value = {"role:write"})
	public void delete(@PathParam("roleId") int roleId, @PathParam("version") int version) {
		Role role = srvAuth.findById(Role.class, roleId);
		srvAuth.deleteRole(role.getId(), version);
	}

	@POST
	@Path("xls")
	@RequiresPermissions(value = {"role:read", "role:write"}, logical = Logical.OR)
	public Response export(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		TableData<Role> data = this.findAll(startIndex, maxRecord, tq);
		RptRoleList rpt = new RptRoleList(data, startIndex, maxRecord);

		return CommonUtil.buildExcelResponse(rpt, "role");
	}
}
