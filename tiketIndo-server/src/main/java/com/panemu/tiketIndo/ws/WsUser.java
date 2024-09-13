package com.panemu.tiketIndo.ws;

import com.panemu.search.TableCriteria;
import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoPasswordTemplate;
import com.panemu.tiketIndo.dto.DtoUserList;
import com.panemu.tiketIndo.dto.DtoUserTemplate;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.Member;
import com.panemu.tiketIndo.rcd.MemberSetting;
import com.panemu.tiketIndo.rcd.Role;
import com.panemu.tiketIndo.rpt.RptUserList;
import com.panemu.tiketIndo.srv.SrvAuth;
import com.panemu.tiketIndo.srv.SrvMember;
import com.panemu.tiketIndo.srv.SrvMemberSetting;
import com.panemu.search.TableQuery;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsUser {

	private static final int SHIRO_INI_SALT_ITERATION = 1024;

	@Inject
	private SrvMember srvMember;
	@PersistenceContext
	EntityManager em;
	private Logger logger = LoggerFactory.getLogger(WsUser.class);
	@Inject
	private SrvAuth srvRole;
	@Inject
	private SrvMemberSetting srvMemberSetting;

	@POST
	@RequiresAuthentication
	public TableData<DtoUserList> findAll(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		ensureAdminOrCanRead();
		TableData<Member> lstResult = srvMember.find(tq, startIndex, maxRecord);
		List<DtoUserList> lst = new ArrayList<>();
		lstResult.getRows().forEach(row -> {
			lst.add(DtoUserList.create(row));
		});
		TableData<DtoUserList> result = new TableData<>(lst, lstResult.getTotalRows());
		return result;
	}

	@POST
	@Path("xls")
	@RequiresAuthentication
	public Response export(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		ensureAdminOrCanRead();
		TableData<DtoUserList> data = this.findAll(startIndex, maxRecord, tq);
		RptUserList rpt = new RptUserList(data, startIndex, maxRecord);

		return CommonUtil.buildExcelResponse(rpt, "member");
	}

	@Path("roles")
	@GET
	@RequiresAuthentication
	public List<Role> getRoleList() {
		//ensureAdminOrCanRead(); no need permission checking. Needed to fill role combobox in user form
		List<Role> lstRole = srvRole.retrieveAll(Role.class);
		for (Role role : lstRole) {
			srvRole.detach(role);
			role.setPermissionList(null);
		}
		return lstRole;
	}

	@Path("byRole/{role}")
	@GET
	@Produces("application/json")
	@RequiresAuthentication
	public List<Member> getUserByRole(@PathParam("role") String role) {
		TableCriteria crit = new TableCriteria();
		crit.setAttributeName("role");
		crit.setValue(role);
		crit.setOperator(TableCriteria.Operator.eq);

		List<TableCriteria> lstFilter = new ArrayList<>();
		lstFilter.add(crit);

		TableQuery tq = new TableQuery();
		tq.setTableCriteria(lstFilter);

		TableData<Member> lstUser = srvMember.find(tq, 0, Integer.MAX_VALUE);
		return lstUser.getRows();
	}

	@Path("byId/{userId}")
	@GET
	@RequiresAuthentication
	public Member getUserById(@PathParam("userId") int userId) {
		ensureAdminOrCanRead();
		Member user = srvMember.getMemberById(userId);
		srvMember.detach(user);
		user.setPassword(null);
		user.setPasswordSalt(null);
		return user;
	}

	@Path("myProfile")
	@GET
	@RequiresAuthentication
	public Member getMyProfile() {
		Subject subject = SecurityUtils.getSubject();
		Member user = srvMember.getMemberByMemberName(subject.getPrincipal() + "");
		srvMember.detach(user);
		user.setPassword(null);
		user.setPasswordSalt(null);
		return user;
	}

	private String generatePassword(String password, String salt) {
		Sha256Hash hash = new Sha256Hash(password, (new SimpleByteSource(salt)).getBytes(), SHIRO_INI_SALT_ITERATION);
		return hash.toHex();
	}

	@POST
	@Path("new")
	@Consumes({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public void createUser(DtoUserTemplate template) {
		ensureAdminOrCanWrite();
		Subject currentUser = SecurityUtils.getSubject();
		String duplicate = "Username";
		Member user = srvMember.getMemberByMemberName(template.username);
		if (user == null) {
			user = srvMember.getMemberByEmail(template.email);
			duplicate = "Email";
			if (user == null) {
				user = new Member();
				user.setUsername(template.username);
				user.setEmail(template.email);
				String salt = createSalt();
				String saltedPassword = generatePassword(template.password, salt);
				user.setPassword(saltedPassword);
				user.setPasswordSalt(salt);
				user.setRole(template.idRole);
				user.setKoordinator(template.koordinator);
				user.setModifiedBy(currentUser.getPrincipal() + "");
				user.setModifiedDate(new Date());
				srvMember.insertMember(user);
			} else {
				throw GeneralException.create(new ErrorEntity(ErrorCode.ER0009, duplicate));
			}
		} else {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0009, duplicate));
		}
	}

	@PUT
	@Path("update")
	@Consumes({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public void updateUser(DtoUserTemplate template) {
		Subject currentUser = SecurityUtils.getSubject();
		Member loggedUser = srvMember.getMemberByMemberName(currentUser.getPrincipal() + "");
		Member userToUpdate = srvMember.getMemberById(template.id);
		srvMember.detach(userToUpdate);
		if (!loggedUser.getId().equals(userToUpdate.getId())) {
			//if not user's own profile, check permission
			ensureAdminOrCanWrite();

			//a user cannot edit his own role. Only other user can (need user:write permission)
			userToUpdate.setRole(template.idRole);

		}

		if (StringUtils.isNotBlank(template.password)) {
			if ("hellocountry".equals(userToUpdate.getUsername())) {
				throw GeneralException.create(new ErrorEntity(ErrorCode.ER0008, null));
			} else {
				String salt = createSalt();
				String saltedPassword = generatePassword(template.password, salt);
				userToUpdate.setPassword(saltedPassword);
				userToUpdate.setPasswordSalt(salt);
			}
		}

		userToUpdate.setEmail(template.email);
		userToUpdate.setVersion(template.version);
		userToUpdate.setModifiedBy(currentUser.getPrincipal() + "");
		userToUpdate.setModifiedDate(new Date());
		userToUpdate.setKoordinator(template.koordinator);
		srvMember.updateMember(userToUpdate);
	}

	@DELETE
	@Path("{id}")
	@RequiresAuthentication
	public void delete(@PathParam("id") Integer id) {
		ensureAdminOrCanWrite();
		Subject currentUser = SecurityUtils.getSubject();
		Member userToDelete = srvMember.getMemberById(id);
		if (userToDelete.getUsername().equals(currentUser.getPrincipal())) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0007, ""));
		}
		if (userToDelete.getUsername().equals("admin")) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0006, ""));
		}

		srvMember.deleteMember(userToDelete);
	}

	@POST
	@Path("updateProfile/{username}/{email}")
	@Consumes({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public void updateProfile(@PathParam("username") String username, @PathParam("email") String email) {
		ensureAdminOrCanWrite();
		Member oldData = srvMember.getMemberByMemberName(username);
		Member toCheck = srvMember.getMemberByEmail(email);
		if (toCheck == null) {
			oldData.setEmail(email);
			srvMember.updateMember(oldData);
		} else {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0005, null));
		}
	}

	@POST
	@Path("changePassword/{userId}")
	@RequiresAuthentication
	public void changePassword(@PathParam("userId") int userId, DtoPasswordTemplate data) {
		ensureAdminOrCanWrite();
		Subject currentUser = SecurityUtils.getSubject();
		Member user = srvMember.getMemberById(userId);
		boolean toReset = false;
		if (currentUser.hasRole("admin") && (!currentUser.getPrincipal().toString().equals(user.getUsername()))) {
			toReset = true;
		}
		if (data.getOldPasswd() != null && !data.getOldPasswd().equals("undefined")) {
			char[] oldPwd = data.getOldPasswd().toCharArray();
			boolean valid = new Sha256Hash(oldPwd).toHex().equals(user.getPassword());
			if (valid) {
				char[] passwd = data.getNewPasswd().toCharArray();
				user.setPassword(new Sha256Hash(passwd).toHex());
				srvMember.updateMember(user);
			} else {
				throw GeneralException.create(new ErrorEntity(ErrorCode.ER0003, null));
			}
		} else if (toReset) {
			//to reset
			char[] passwd = data.getNewPasswd().toCharArray();
			user.setPassword(new Sha256Hash(passwd).toHex());
			srvMember.updateMember(user);
		} else {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0004, null));
		}

	}

	private String createSalt() {
		String salt = new BigInteger(250, new SecureRandom()).toString(32);
		return salt;
	}

	@POST
	@Path("setting")
	@Consumes({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public void saveMemberSetting(MemberSetting us) {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		if (username == null) {
			return;
		}

		MemberSetting fromDb = srvMemberSetting.find(username, us.getCategory(), us.getName());
		if (fromDb != null) {
			fromDb.setValue(us.getValue());
			srvMemberSetting.update(fromDb);
		} else {
			us.setUsername(username);
			srvMemberSetting.insert(us);
		}
	}

	@GET
	@Path("setting/{category}")
	@RequiresAuthentication
	public List<MemberSetting> getMemberSetting(@PathParam("category") String category) {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		List<MemberSetting> lst = srvMemberSetting.find(username, category);
		return lst;
	}

	@POST
	@Path("setting/del")
	@RequiresAuthentication
	public void deleteMemberSetting(MemberSetting us) {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		if (username == null) {
			return;
		}

		MemberSetting fromDb = srvMemberSetting.find(username, us.getCategory(), us.getName());
		if (fromDb != null) {
			srvMemberSetting.delete(fromDb);
		}
	}

	@GET
	@Path("lstSeller")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public List<DtoUserList> getVenueName() {
		List<DtoUserList> lstdto = new ArrayList<>();
		DtoUserList dto = null;
		List<Member> lst = srvMember.getLstSeller();
		for (Member rcd : lst) {
			dto = DtoUserList.create(rcd);
			lstdto.add(dto);
		}
		return lstdto;
	}

	private void ensureAdminOrCanRead() {
		Subject subject = SecurityUtils.getSubject();
		if (subject == null || !subject.isAuthenticated()) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0403, ""));
		}
		if (subject.hasRole("admin")
				  || subject.isPermitted("user:read")
				  || subject.isPermitted("user:write")) {
			return;
		}
		throw GeneralException.create(new ErrorEntity(ErrorCode.ER0403, ""));
	}

	private void ensureAdminOrCanWrite() {
		Subject subject = SecurityUtils.getSubject();
		if (subject == null || !subject.isAuthenticated()) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0403, ""));
		}
		if (subject.hasRole("admin")
				  || subject.isPermitted("user:write")) {
			return;
		}
		throw GeneralException.create(new ErrorEntity(ErrorCode.ER0403, ""));
	}
}
