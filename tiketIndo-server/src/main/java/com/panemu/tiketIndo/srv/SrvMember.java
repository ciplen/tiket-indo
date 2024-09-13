/*
 * Intelectual Property of Panemu
 * http://panemu.com
 */
package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.Member;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Stateless
public class SrvMember extends SrvBase {

	private static Logger logger = LoggerFactory.getLogger(SrvMember.class);

	public Member insertMember(Member member) {
		return super.insert(member);
	}

	public Member updateMember(Member member) {
		return super.update(member);
	}

	public void deleteMember(Member member) {
		super.delete(member);
	}

	public void deleteMembers(List<Member> members) {
		super.delete(members);
	}

	public TableData<Member> find(TableQuery query, int startIndex, int maxRecord) {
		try {
			for (TableCriteria crit : query.getTableCriteria()) {
				crit.setTableAlias("ui");
				if ("role".equals(crit.getAttributeName())) {
					crit.setAttributeName("role.role");
				}
			}
			for (SortingInfo si : query.getSortingInfos()) {
				si.setTableAlias("ui");
				if ("role".equals(si.getAttributeName())) {
					si.setAttributeName("role.role");
				}
			}

			String whereClause = query.generateWhereClause(true);
			String orderClause = query.generateOrderByClause(true);
			TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from Member ui" + whereClause, Long.class);
			query.applyParameter(countQuery);
			long totalRow = countQuery.getSingleResult();

			TypedQuery<Member> typedQuery = em.createQuery("SELECT ui from Member ui " + whereClause + orderClause, Member.class);
			query.applyParameter(typedQuery);
			typedQuery.setFirstResult(startIndex);
			if (maxRecord > 0) {
				typedQuery.setMaxResults(maxRecord);
			}
			List<Member> resultList = typedQuery.getResultList();
			TableData<Member> td = new TableData<>(resultList, totalRow);
			return td;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
		}
	}

	public boolean resetPassword(Member member, String newPassword) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Member> cq = builder.createQuery(Member.class);
		Root<Member> root = cq.from(Member.class);
		Predicate predicate = builder.equal(root.get("username"), member.getUsername());
		cq.select(root);
		cq.where(predicate);
		TypedQuery<Member> typedQuery = em.createQuery(cq);
		Member changedMember = typedQuery.getSingleResult();
		if (changedMember == null) {
			return false;
		}
		char[] password = newPassword.toCharArray();
		changedMember.setPassword(new Sha256Hash(password).toHex());
		super.update(changedMember);
		return true;
	}

	public Member getMemberByMemberName(String username) {
		String sql = "SELECT e FROM " + Member.class.getName() + " e WHERE username = :param_username";

		Query q = em.createQuery(sql);
		q.setParameter("param_username", username);
		try {
			return (Member) q.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

	public Member getMemberByEmail(String email) {
		Query q = em.createQuery("SELECT e FROM " + Member.class.getName() + " e WHERE email = :email");
		q.setParameter("email", email);
		try {
			return (Member) q.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

	public Member getMemberById(Integer id) {
		Query q = em.createQuery("SELECT e FROM " + Member.class.getName()
				  + " e WHERE e.id = :param_id");
		q.setParameter("param_id", id);
		try {
			return (Member) q.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

	public List<Member> getLstSeller() {
		Query q = em.createQuery("SELECT e FROM " + Member.class.getName()
				  + " e WHERE e.role = 'koordinator' OR e.role='reseller'");
		try {
			return q.getResultList();
		} catch (NoResultException exc) {
			exc.printStackTrace();
			return null;
		}
	}

}
