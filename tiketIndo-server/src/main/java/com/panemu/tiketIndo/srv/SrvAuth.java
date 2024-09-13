package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.Permission;
import com.panemu.tiketIndo.rcd.Role;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Stateless
public class SrvAuth extends SrvBase {

	@PersistenceContext
	private EntityManager em;
	private Logger logger = LoggerFactory.getLogger(SrvAuth.class);
	
	public Role insertRole(Role role) {
		return super.insert(role);
	}

	public Role updateRole(Role role) {
		return super.update(role);
	}

	public void deleteRole(int id, int version) {
		Role role = em.find(Role.class, id);
		em.detach(role);
		if ("admin".equals(role.getName())) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "admin role cannot be deleted"));
		}
		role.setVersion(version);
		super.delete(role);
	}

	public List<Role> findRoles() {
		return em.createQuery("Select r from Role r", Role.class).getResultList();
	}

	public List<Permission> findPermissions() {
		return em.createQuery("Select p from Permission p", Permission.class).getResultList();
	}

	public List<Permission> findPermissionByModule(String module) {
		TypedQuery<Permission> q = em.createQuery("SELECT p from Permission p where p.module = :module", Permission.class);
		q.setParameter("module", module);
		List<Permission> resultList = q.getResultList();
		return resultList;
	}

	public List<Permission> findPermissionByCode(List<String> lstCode) {
		TypedQuery<Permission> q = em.createQuery("SELECT p from Permission p where p.code in (:param_code)", Permission.class);
		q.setParameter("param_code", lstCode);
		List<Permission> resultList = q.getResultList();
		return resultList;
	}

	public List<String> findModule() {
		TypedQuery<String> q = em.createQuery("SELECT distinct p.module from Permission p", String.class);
		List<String> resultList = q.getResultList();
		return resultList;
	}

	public Role findRole(int id) {
		TypedQuery<Role> q = em.createQuery("Select r from Role r LEFT JOIN FETCH r.permissionList where r.id = :id ", Role.class);
		q.setParameter("id", id);
		try {
			return q.getSingleResult();
		} catch (Exception ex) {
			logger.error("Unable to retrieve role id: " + id + ". " + ex.getMessage(), ex);
			return null;
		}
	}
	
	public TableData<Role> findRole(TableQuery query, int startIndex, int maxRecord) {
		try {
			for (TableCriteria crit : query.getTableCriteria()) {
				crit.setTableAlias("ui");
//				if ("isSuperAdmin".equals(crit.getAttributeName())) {
//					crit.setSearchModeToBoolean();
//				}
			}
			for (SortingInfo si : query.getSortingInfos()) {
				si.setTableAlias("ui");
			}

			String whereClause = query.generateWhereClause(true);
			String orderClause = query.generateOrderByClause(true);
			TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from Role ui " + whereClause, Long.class);
			query.applyParameter(countQuery);
			long totalRow = countQuery.getSingleResult();

			TypedQuery<Role> typedQuery = em.createQuery("SELECT ui from Role ui " + whereClause + orderClause, Role.class);
			query.applyParameter(typedQuery);
			typedQuery.setFirstResult(startIndex);
			if (maxRecord > 0) {
				typedQuery.setMaxResults(maxRecord);
			}
			List<Role> resultList = typedQuery.getResultList();
			TableData<Role> td = new TableData<>(resultList, totalRow);
			return td;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
		}
	}
}
