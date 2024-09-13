package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.MemberSetting;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Stateless
public class SrvMemberSetting extends SrvBase {

	private static Logger logger = LoggerFactory.getLogger(SrvMember.class);

	public MemberSetting find(String username, String category, String name) {
		Query q = em.createQuery("SELECT e FROM " + MemberSetting.class.getName() + " e WHERE username = :username "
				  + "and category = :category and name = :name");
		q.setParameter("username", username);
		q.setParameter("category", category);
		q.setParameter("name", name);
		try {
			return (MemberSetting) q.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}

	}

	public List<MemberSetting> find(String username, String category) {
		TypedQuery q = em.createQuery("SELECT e FROM " + MemberSetting.class.getName() + " e WHERE username = :username "
				  + "and category = :category order by name", MemberSetting.class);
		q.setParameter("username", username);
		q.setParameter("category", category);
		return q.getResultList();
	}

	public MemberSetting update(MemberSetting record) {
		record.setModifiedDate(new Date());
		return super.update(record);
	}

	public MemberSetting insert(MemberSetting record) {
		record.setModifiedDate(new Date());
		return super.insert(record);
	}

	public MemberSetting delete(MemberSetting record) {
		return super.delete(record);
	}
}
